package com.danosoftware.galaxyforce.services.googleplay;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.danosoftware.galaxyforce.options.OptionGooglePlay;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;
import java.util.Set;

import static com.danosoftware.galaxyforce.constants.GameConstants.RC_SIGN_IN;

public class GooglePlayServices {

    /* logger tag */
    private static final String ACTIVITY_TAG = "GooglePlayServices";

    private final Activity mActivity;
    private final ConfigurationService configurationService;
    private final GoogleSignInClient signInClient;
    private final GoogleSignInOptions signInOptions;
    private volatile ConnectionState connectedState;

    /*
     * set of observers to be notified following any connection state changes.
     */
    private final Set<GooglePlayObserver> observers;

    public GooglePlayServices(
            final Activity activity,
            final ConfigurationService configurationService) {
        Log.d(ACTIVITY_TAG, "Creating Billing client.");
        this.mActivity = activity;
        this.configurationService = configurationService;
        this.observers = new HashSet<>();
        this.signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                        // Add the APPFOLDER scope for Snapshot support.
                        .requestScopes(Drive.SCOPE_APPFOLDER)
                        // below scopes are replace the above deprecated scope
//                        .requestScopes(new Scope(DriveScopes.DRIVE_APPDATA))
//                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
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
     * observers in connection callback threads.
     */
    public synchronized void registerConnectionObserver(GooglePlayObserver observer) {
        Log.d(ACTIVITY_TAG, "Register Google Service Observer '" + observer + "'.");
        observers.add(observer);
    }

    /*
     * Unregister an observer for any connection state refreshes. Normally called
     * when a observer is disposed.
     *
     * Synchronized to avoid removing observer in main thread while notifying
     * observers in connection callback threads.
     */
    public synchronized void unregisterConnectionObserver(GooglePlayObserver observer) {
        Log.d(ACTIVITY_TAG, "Unregister Google Service Observer '" + observer + "'.");
        observers.remove(observer);
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
        connectedState = ConnectionState.CONNECTED; // TODO do we need this???
        notifyObservers(connectionRequest, ConnectionState.CONNECTED);
        showSavedGamesUI();
    }

    /**
     * Called following any disconnection from Google Play Services.
     * This includes failed log-ins or successful log-outs.
     */
    private void onDisconnected(ConnectionRequest connectionRequest) {
        connectedState = ConnectionState.DISCONNECTED; // TODO do we need this???
        notifyObservers(connectionRequest, ConnectionState.DISCONNECTED);
    }

    /**
     * Notify observers of the latest connection state.
     *
     * Synchronized to avoid sending notifications to observers while observers
     * are being added/removed in another thread.
     */
    private synchronized void notifyObservers(
            ConnectionRequest connectionRequest,
            ConnectionState connectionState) {
        for (GooglePlayObserver observer : observers) {
            Log.i(ACTIVITY_TAG, "Sending Connection State Change " + connectionState.name() + " to " + observer);
            observer.onConnectionStateChange(connectionRequest, connectionState);
        }
    }

    /**
     * Attempt to sign-in without interupting the user.
     * If previous attempts have resulted in us being disconected,
     * do not try again.
     */
    public void signInSilently() {

        if (configurationService.getGooglePlayOption() == OptionGooglePlay.OFF) {
            Log.i(ACTIVITY_TAG, "User has previously chosen to sign-out. We will not attempt to sign-in silently.");
            onDisconnected(ConnectionRequest.LOG_IN);
            return;
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivity);
        if (account != null && GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            Log.i(ACTIVITY_TAG, "Already signed-in");
            // Already signed in.
            onConnected(account, ConnectionRequest.LOG_IN);
        } else {
            // Not signed-in. Try the silent sign-in first.
            signInClient.silentSignIn().addOnCompleteListener(mActivity,
                    new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                            if (task.isSuccessful()) {
                                Log.i(ACTIVITY_TAG, "Success signInSilently");
                                GoogleSignInAccount signedInAccount = task.getResult();
                                onConnected(signedInAccount, ConnectionRequest.LOG_IN);
                            } else {
                                Log.w(ACTIVITY_TAG, "Failed signInSilently");
                                onDisconnected(ConnectionRequest.LOG_IN);
//                                connectedState = ConnectionState.DISCONNECTED;
//                                startSignInIntent();
                            }
                        }
                    });
        }
    }

    public void startSignInIntent() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivity);
        if (account != null && GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            Log.i(ACTIVITY_TAG, "Already signed-in");
            // Already signed in.
            onConnected(account, ConnectionRequest.LOG_IN);
            return;
        }

        Log.d(ACTIVITY_TAG, "startSignInIntent()");
        Intent intent = signInClient.getSignInIntent();

        // invoke manual sign-on using our activity.
        // on-completion, this will call onActivityResult() within our activity.
        // this will in-turn pass the result back to our handleSignInResult()
        mActivity.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

//        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//        result.
//        if (result.isSuccess()) {
//            // The signed in account is stored in the result.
//            GoogleSignInAccount signedInAccount = result.getSignInAccount();
////                GamesClient gamesClient = Games.getGamesClient(this, signedInAccount);
////                gamesClient.setViewForPopups(this.findViewById(android.R.id.content));
//        } else {
//            String message = result.getStatus().getStatusMessage();
//            if (message == null || message.isEmpty()) {
//                message = "Unknown";
//            }
////                new AlertDialog.Builder(this).setMessage(message)
////                        .setNeutralButton(android.R.string.ok, null).show();
//        }




//        if(completedTask.isSuccessful()) {
//            GoogleSignInAccount signedInAccount = completedTask.getResult();
//            Log.i(ACTIVITY_TAG, "signInResult:success");
//            onConnected(signedInAccount);
//        } else {
//            String message = completedTask.getStatus().getStatusMessage();
//            if (message == null || message.isEmpty()) {
//                message = "Unknown";
//            }
//                new AlertDialog.Builder(mActivity).setMessage(message)
//                        .setNeutralButton(android.R.string.ok, null).show();
//        }
//            Exception exception = completedTask.getException();
//            if (exception instanceof ApiException) {
//                final ApiException apiException = (ApiException) exception;
//                final int exceptionCode = apiException.getStatusCode();
//                final String exceptionCause = CommonStatusCodes.getStatusCodeString(exceptionCode);
//                Log.w(ACTIVITY_TAG, String.format(
//                        "signInResult:failure code={}. reason={}",
//                        exceptionCode,
//                        exceptionCause));
//                connectedState = ConnectedState.DISCONNECTED;
//            } else {
//                Log.w(ACTIVITY_TAG, String.format(
//                        "signInResult:failure reason={}",
//                        exception.getMessage()));
//            }
//        }


        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.i(ACTIVITY_TAG, "signInResult:success");
            onConnected(account, ConnectionRequest.LOG_IN);
        } catch (ApiException e) {
            Log.w(ACTIVITY_TAG, "signInResult:failed code=" + e.getStatusCode());
            onDisconnected(ConnectionRequest.LOG_IN);
//            connectedState = ConnectionState.DISCONNECTED;
//            signInSilently();
        }
    }

    public void signOut() {
        signInClient.signOut().addOnCompleteListener(mActivity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d(ACTIVITY_TAG, "signOut(): " + (successful ? "success" : "failed"));
                        onDisconnected(ConnectionRequest.LOG_OUT);
                    }
                });
    }

    private void showSavedGamesUI() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivity);
        SnapshotsClient snapshotsClient =
                Games.getSnapshotsClient(mActivity, account);
//        int maxNumberOfSavedGamesToShow = 5;
//
//        Task<Intent> intentTask = snapshotsClient.getSelectSnapshotIntent(
//                "See My Saves", true, true, maxNumberOfSavedGamesToShow);
//
//        intentTask.addOnSuccessListener(new OnSuccessListener<Intent>() {
//            @Override
//            public void onSuccess(Intent intent) {
//                mActivity.startActivityForResult(intent, RC_SAVED_GAMES);
//            }
//        });

//        intentTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                throw new RuntimeException("error", e);
//            }
//        });
    }

    /**
     * This callback will be triggered after you call startActivityForResult from the
     * showSavedGamesUI method.
     */
    public void handleSavedGame(Intent intent) {
        String mCurrentSaveName = "snapshotTemp";
        if (intent != null) {
//            if (intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA)) {
//                // Load a snapshot.
//                SnapshotMetadata snapshotMetadata =
//                        intent.getParcelableExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA);
//                mCurrentSaveName = snapshotMetadata.getUniqueName();
//
//                // Load the game data from the Snapshot
//                // ...
//            } else if (intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_NEW)) {
//                // Create a new snapshot named with a unique string
//                String unique = new BigInteger(281, new Random()).toString(13);
//                mCurrentSaveName = "snapshotTemp-" + unique;
//
//                // Create the new snapshot
//                // ...
//            }
        }
    }
}
