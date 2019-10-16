package com.danosoftware.galaxyforce.services.googleplay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.danosoftware.galaxyforce.R;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.options.OptionGooglePlay;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.danosoftware.galaxyforce.constants.GameConstants.RC_SIGN_IN;
import static com.danosoftware.galaxyforce.constants.GameConstants.SAVED_GAME_FILENAME;

/**
 * Services responsible for connecting to Google Play Services and
 * managing Saved Games.
 */
public class GooglePlayServices {

    /* logger tag */
    private static final String ACTIVITY_TAG = "GooglePlayServices";

    private static final int MAX_SNAPSHOT_RESOLVE_RETRIES = 10;

    private final Activity mActivity;
    private final ConfigurationService configurationService;
    private final GoogleSignInClient signInClient;
    private volatile ConnectionState connectedState;

    /*
     * set of observers to be notified following any connection state changes.
     */
    private final Set<GooglePlayConnectionObserver> connectionObservers;

    /*
     * saved game service to be notified following any saved game loads.
     */
    private volatile SavedGame savedGame;

    public GooglePlayServices(
            final Activity activity,
            final ConfigurationService configurationService) {
        this.mActivity = activity;
        this.configurationService = configurationService;
        this.connectionObservers = new HashSet<>();
        final GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                        .requestScopes(new Scope(DriveScopes.DRIVE_APPDATA))
                        .build();
        this.signInClient = GoogleSignIn.getClient(
                activity,
                signInOptions);
        this.connectedState = ConnectionState.NO_ATTEMPT;
    }

    /**
     * Return current connection state.
     */
    public synchronized ConnectionState connectedState() {
        return connectedState;
    }

    /*
     * Register an observer for any connection state changes. Normally called
     * when a observer is constructed.
     *
     * Synchronized to avoid adding observer in main thread while notifying
     * connectionObservers in connection callback threads.
     */
    public synchronized void registerConnectionObserver(GooglePlayConnectionObserver observer) {
        Log.d(ACTIVITY_TAG, "Register Google Service Observer '" + observer + "'.");
        connectionObservers.add(observer);
    }

    /*
     * Unregister an observer for any connection state refreshes. Normally called
     * when a observer is disposed.
     *
     * Synchronized to avoid removing observer in main thread while notifying
     * connectionObservers in connection callback threads.
     */
    public synchronized void unregisterConnectionObserver(GooglePlayConnectionObserver observer) {
        Log.d(ACTIVITY_TAG, "Unregister Google Service Observer '" + observer + "'.");
        connectionObservers.remove(observer);
    }

    /*
     * Register the save game service. This will be notified for any saved game loads.
     * Normally called when save game is constructed.
     */
    public void registerSavedGameService(SavedGame saveGameSvc) {
        Log.d(ACTIVITY_TAG, "Register Saved Game Service '" + saveGameSvc + "'.");
        this.savedGame = saveGameSvc;
    }

    /**
     * Called following any successful connection to Google Play Services.
     */
    private void onConnected(
            GoogleSignInAccount signedInAccount,
            ConnectionRequest connectionRequest) {

        // set view for any google-play pop-ups
        GamesClient gamesClient = Games.getGamesClient(mActivity, signedInAccount);
        gamesClient.setViewForPopups(mActivity.findViewById(android.R.id.content));
        connectedState = ConnectionState.CONNECTED;
        notifyConnectionObservers(connectionRequest, ConnectionState.CONNECTED);
    }

    /**
     * Called following any disconnection from Google Play Services.
     * This includes failed log-ins or successful log-outs.
     */
    private void onDisconnected(ConnectionRequest connectionRequest) {
        connectedState = ConnectionState.DISCONNECTED;
        notifyConnectionObservers(connectionRequest, ConnectionState.DISCONNECTED);
    }

    /**
     * Notify observers of the latest connection state.
     * <p>
     * Synchronized to avoid sending notifications to observers while observers
     * are being added/removed in another thread.
     */
    private synchronized void notifyConnectionObservers(
            ConnectionRequest connectionRequest,
            ConnectionState connectionState) {
        for (GooglePlayConnectionObserver observer : connectionObservers) {
            Log.i(ACTIVITY_TAG, "Sending Connection State Change " + connectionState.name() + " to " + observer);
            observer.onConnectionStateChange(connectionRequest, connectionState);
        }
    }

    /**
     * Attempt to sign-in without interrupting the user.
     * If previous attempts have resulted in us being disconected,
     * do not try again.
     */
    public void signInSilently() {

        if (configurationService.getGooglePlayOption() == OptionGooglePlay.OFF) {
            Log.i(ACTIVITY_TAG, "User has previously chosen to sign-out. We will not attempt to sign-in silently.");
            onDisconnected(ConnectionRequest.LOG_IN);
            return;
        }

        // start async login and snapshot loading (using silent sign-in)
        loginAndLoadSnapshotAsync(
                silentSignInTask()
        );
    }

    /**
     * Initiate a manual sign-in to Google PLay Services
     */
    public void startSignInIntent() {
        Log.d(ACTIVITY_TAG, "startSignInIntent()");
        Intent intent = signInClient.getSignInIntent();

        // invoke manual sign-on using our activity.
        // on-completion, this will call onActivityResult() within our activity.
        // this will in-turn pass the result back to our handleSignInResult()
        mActivity.startActivityForResult(intent, RC_SIGN_IN);
    }

    /**
     * Handles the response following a sign-in attempt.
     */
    public void handleSignInResult(Task<GoogleSignInAccount> signInTask) {
        signInTask
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.i(ACTIVITY_TAG, "signInResult:success");
                        onConnected(googleSignInAccount, ConnectionRequest.LOG_IN);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception ex) {
                        final String reason = extractApiFailure(ex);
                        Log.w(ACTIVITY_TAG, "signInResult:failed=" + reason);
                        onDisconnected(ConnectionRequest.LOG_IN);
                        new AlertDialog.Builder(mActivity)
                                .setMessage("Sign-in failed. Please try again later.")
                                .setNeutralButton(android.R.string.ok, null)
                                .show();

                    }
                });

        // start async login and snapshot loading (using supplied sign-in task)
        loginAndLoadSnapshotAsync(
                signInTask
        );
    }

    /**
     * Disconnect from Google Play Services
     */
    public void signOut() {
        signInClient.signOut()
                .addOnCompleteListener(
                        mActivity,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                boolean successful = task.isSuccessful();
                                Log.d(ACTIVITY_TAG, "signOut(): " + (successful ? "success" : "failed"));
                                onDisconnected(ConnectionRequest.LOG_OUT);
                            }
                        });
    }

    /**
     * Save game progress.
     */
    public void saveGame(final GooglePlaySavedGame savedGame) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivity);
        if (connectedState != ConnectionState.CONNECTED || account == null) {
            // we are no longer signed-in. Saving game is impossible.
            Log.d(ACTIVITY_TAG, "Save Game Unavailable. User is not signed-in.");
            return;
        }

        // start async save game
        saveSnapshotAsync(account, savedGame);
    }

    /**
     * Asynchronously sign-in to Google Play Services.
     * Then Asynchronously load latest snapshot and resolve conflicts.
     * Then asynchronously overwrite snapshot with current game progress.
     * <p>
     * If current device's game progress is on a higher wave than the the wave
     * stored in Google Play, then immediately overwrite the Google Play's snapshot
     * so other devices can see this progress.
     */
    private void loginAndLoadSnapshotAsync(Task<GoogleSignInAccount> signInTask) {

        // attempt to sign-in to Google Play
        signInTask
                // then attempt to load snapshot and resolve conflicts
                .continueWithTask(new Continuation<GoogleSignInAccount, Task<Snapshot>>() {
                    @Override
                    public Task<Snapshot> then(@NonNull Task<GoogleSignInAccount> accountTask) throws Exception {
                        return loadSnapshotAndResolveConflictsTask(accountTask.getResult());
                    }
                })
                // then attempt to save snapshot if player has progressed beyond current saved snapshot
                .continueWithTask(new Continuation<Snapshot, Task<SnapshotMetadata>>() {
                    @Override
                    public Task<SnapshotMetadata> then(@NonNull Task<Snapshot> snapshotTask) throws Exception {
                        Snapshot snapshot = snapshotTask.getResult();
                        GooglePlaySavedGame googleSavedGame = extractSavedGame(snapshot);
                        Log.i(ACTIVITY_TAG, "Loaded wave " + googleSavedGame.getHighestWaveReached());

                        // tell SavedGame service of latest saved game loaded and ask for highest wave reached.
                        if (savedGame != null) {
                            GooglePlaySavedGame deviceSavedGame = savedGame.computeHighestWaveOnSavedGameLoaded(googleSavedGame);

                            // if player has already progressed beyond cloud's saved game on local device then update cloud save
                            if (deviceSavedGame.getHighestWaveReached() > googleSavedGame.getHighestWaveReached()) {
                                return writeSnapshotTask(snapshot, deviceSavedGame);
                            }
                        }
                        // otherwise just return task for current snapshot without saving anything
                        SnapshotsClient snapshotsClient =
                                Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity));
                        snapshotsClient.discardAndClose(snapshot);
                        return Tasks.forResult(snapshot.getMetadata());
                    }
                })
                // all requests successful
                .addOnSuccessListener(new OnSuccessListener<SnapshotMetadata>() {
                    @Override
                    public void onSuccess(SnapshotMetadata snapshotMetadata) {
                        Log.i(ACTIVITY_TAG, "Successful login and saved game load.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(ACTIVITY_TAG, "Failed login and saved game load.", e);
                    }
                });
    }

    /**
     * Asynchronously load latest snapshot and resolve conflicts.
     * Then asynchronously overwrite snapshot with current game progress
     * .
     *
     * @param account   - player's account
     * @param savedGame - latest game progress to save
     */
    private void saveSnapshotAsync(
            final GoogleSignInAccount account,
            final GooglePlaySavedGame savedGame) {

        loadSnapshotAndResolveConflictsTask(account)
                .continueWithTask(new Continuation<Snapshot, Task<SnapshotMetadata>>() {
                    @Override
                    public Task<SnapshotMetadata> then(@NonNull Task<Snapshot> snapshotTask) throws Exception {
                        return writeSnapshotTask(snapshotTask.getResult(), savedGame);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<SnapshotMetadata>() {
                    @Override
                    public void onSuccess(SnapshotMetadata snapshotMetadata) {
                        int wave = savedGame != null ? savedGame.getHighestWaveReached() : 0;
                        Log.i(ACTIVITY_TAG, "Game Saved: Wave " + wave);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(ACTIVITY_TAG, "Game Saved Failed", e);
                    }
                });
    }

    /**
     * Return a task that will asynchronously attempt to sign-in to Google Play Services.
     */
    private Task<GoogleSignInAccount> silentSignInTask() {
        return signInClient.silentSignIn()
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.i(ACTIVITY_TAG, "Success signedIn");
                        onConnected(googleSignInAccount, ConnectionRequest.LOG_IN);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(ACTIVITY_TAG, "Failed signIn", e);
                        onDisconnected(ConnectionRequest.LOG_IN);
                    }
                });
    }

    /**
     * Load the latest snapshot and resolve any conflicts (if any).
     */
    private Task<Snapshot> loadSnapshotAndResolveConflictsTask(GoogleSignInAccount account) {

        // Get the SnapshotsClient from the signed in account.
        SnapshotsClient snapshotsClient =
                Games.getSnapshotsClient(mActivity, account);

        // Open the saved game using its name.
        return snapshotsClient
                .open(SAVED_GAME_FILENAME, true)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(ACTIVITY_TAG, "Error while opening Snapshot.", e);
                    }
                })
                .continueWithTask(
                        new Continuation<
                                SnapshotsClient.DataOrConflict<Snapshot>,
                                Task<Snapshot>>() {
                            @Override
                            public Task<Snapshot> then(
                                    @NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task)
                                    throws Exception {
                                return processSnapshotAndResolveConflictsTask(
                                        task.getResult(),
                                        MAX_SNAPSHOT_RESOLVE_RETRIES);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Snapshot>() {
                    @Override
                    public void onSuccess(Snapshot snapshot) {
                        Log.i(ACTIVITY_TAG, "Successfully opened Snapshot.");
                    }
                });
    }

    /**
     * Recursive function that resolves conflicts until snapshot is no longer in conflict.
     * Will eventually give-up if snapshot is still in conflict after a supplied number of re-tries.
     */
    private Task<Snapshot> processSnapshotAndResolveConflictsTask(
            final SnapshotsClient.DataOrConflict<Snapshot> result,
            final int retryCount) {

        if (!result.isConflict()) {
            // There was no conflict, so return the result of the source.
            TaskCompletionSource<Snapshot> source = new TaskCompletionSource<>();
            source.setResult(result.getData());
            return source.getTask();
        }

        // There was a conflict - we need to resolve it.
        SnapshotsClient.SnapshotConflict conflict = result.getConflict();

        Snapshot snapshot = conflict.getSnapshot();
        Snapshot conflictSnapshot = conflict.getConflictingSnapshot();
        GooglePlaySavedGame savedGame = extractSavedGame(snapshot);
        GooglePlaySavedGame conflictSavedGame = extractSavedGame(conflictSnapshot);

        // Resolve between conflicts by selecting the snapshots with the most progress.
        Snapshot resolvedSnapshot = snapshot;
        if (savedGame != null
                && conflictSavedGame != null
                && conflictSavedGame.getHighestWaveReached() > savedGame.getHighestWaveReached()) {
            resolvedSnapshot = conflictSnapshot;
        } else if (savedGame == null
                && conflictSavedGame != null) {
            resolvedSnapshot = conflictSnapshot;
        }

        return Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity))
                .resolveConflict(conflict.getConflictId(), resolvedSnapshot)
                .continueWithTask(
                        new Continuation<
                                SnapshotsClient.DataOrConflict<Snapshot>,
                                Task<Snapshot>>() {
                            @Override
                            public Task<Snapshot> then(
                                    @NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task)
                                    throws Exception {
                                // Resolving the conflict may cause another conflict,
                                // so recurse and try another resolution.
                                if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES) {
                                    return processSnapshotAndResolveConflictsTask(task.getResult(), retryCount + 1);
                                } else {
                                    throw new GalaxyForceException("Could not resolve snapshot conflicts");
                                }
                            }
                        });
    }


    /**
     * Returns a task to write the save game snapshot with a suitable description.
     */
    private Task<SnapshotMetadata> writeSnapshotTask(
            Snapshot snapshot,
            GooglePlaySavedGame savedGame) throws JsonProcessingException {

        // Set the data payload for the snapshot
        final ObjectMapper mapper = new ObjectMapper();
        final byte[] gameData = mapper.writeValueAsBytes(savedGame);
        snapshot.getSnapshotContents().writeBytes(gameData);

        final int waveUnlocked = savedGame.getHighestWaveReached();
        final boolean finishedAllWaves = waveUnlocked > GameConstants.MAX_WAVES;

        final String description = finishedAllWaves ?
                String.format(
                        "%s: Completed All Waves",
                        mActivity.getString(R.string.app_name)) :
                String.format(
                        "%s: Unlocked Wave %d",
                        mActivity.getString(R.string.app_name),
                        waveUnlocked);

        // Create the change operation
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                .setDescription(description)
                .build();

        SnapshotsClient snapshotsClient =
                Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity));

        // Commit the operation
        return snapshotsClient.commitAndClose(snapshot, metadataChange)
                .addOnSuccessListener(new OnSuccessListener<SnapshotMetadata>() {
                    @Override
                    public void onSuccess(SnapshotMetadata snapshotMetadata) {
                        Log.i(ACTIVITY_TAG, "Gamed saved. Wave: " + waveUnlocked);
                    }
                });
    }

    /**
     * Extract the save game details from the supplied snapshot.
     */
    private GooglePlaySavedGame extractSavedGame(final Snapshot snapshot) {
        try {
            byte[] snapshotContents = snapshot.getSnapshotContents().readFully();
            ObjectMapper mapper = new ObjectMapper();
            if (snapshotContents.length == 0) {
                return new GooglePlaySavedGame(1);
            } else {
                return mapper.readValue(snapshotContents, GooglePlaySavedGame.class);
            }
        } catch (IOException e) {
            Log.e(ACTIVITY_TAG, "Failed to read Saved Game", e);
            return null;
        }
    }

    /**
     * Extract a reason for a ApiException
     * This is not designed to be readable/understandable to a player
     * and should only be used in logging/debugging.
     */
    private String extractApiFailure(Exception exception) {
        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            final int code = apiException.getStatusCode();
            String message = GamesClientStatusCodes.getStatusCodeString(code);
            if (message == null || message.isEmpty()) {
                message = "Unknown Failure:" + code;
            }
            return message;
        }
        return "Unknown Failure:" + exception.getMessage();
    }
}
