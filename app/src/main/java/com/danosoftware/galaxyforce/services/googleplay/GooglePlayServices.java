package com.danosoftware.galaxyforce.services.googleplay;

import static com.danosoftware.galaxyforce.constants.GameConstants.SAVED_GAME_FILENAME;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.danosoftware.galaxyforce.R;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.games.AuthenticationResult;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Services responsible for connecting to Google Play Services and managing Saved Games.
 */
public class GooglePlayServices {

  /* logger tag */
  private static final String ACTIVITY_TAG = "GoogleGamePlayServices";

  private static final int RC_ACHIEVEMENT_UI = 9003;

  private static final int MAX_SNAPSHOT_RESOLVE_RETRIES = 10;

  private final Activity mActivity;
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
      final Activity activity) {
    this.mActivity = activity;
    this.connectionObservers = new HashSet<>();
    this.connectedState = ConnectionState.NO_ATTEMPT;
    PlayGamesSdk.initialize(mActivity);
    authenticate();
  }

  /**
   * Attempt to authenticate the player with Google Play Games Services. This should be called when
   * the game starts to determine if we have access to Google Play Games Services.
   */
  private void authenticate() {
    GamesSignInClient gamesSignInClient = PlayGames
        .getGamesSignInClient(mActivity);
    gamesSignInClient
        .isAuthenticated()
        .addOnCompleteListener(authenticationTask());
  }

  /**
   * Attempt to manually sign-in the player to Google Game Play Services. Normally only required if
   * the initial automatic sign-in failed.
   */
  public void signIn() {
    GamesSignInClient gamesSignInClient = PlayGames
        .getGamesSignInClient(mActivity);
    gamesSignInClient
        .signIn()
        .addOnCompleteListener(authenticationTask())
        .addOnSuccessListener((task) -> showToastSignInMessage())
        .addOnFailureListener((task) -> showToastSignInMessage());
  }

  private void showToastSignInMessage() {
    if (connectedState == ConnectionState.AUTHENTICATED) {
      Toast.makeText(
              mActivity, "Sign-in succeeded",
              Toast.LENGTH_LONG)
          .show();
    } else if (connectedState == ConnectionState.FAILED) {
      Toast.makeText(
              mActivity, "Sign-in failed. Please try again later",
              Toast.LENGTH_LONG)
          .show();
    }
  }

  /*
   * Task that will verify if the user successfully authenticated to
   * Google Play Games Service.
   * Used by authenticate() and signIn() methods.
   *
   * Once authenticated, we will attempt to load the latest saved game snapshot.
   */
  private OnCompleteListener<AuthenticationResult> authenticationTask() {
    return (isAuthenticatedTask) -> {
      boolean isAuthenticated =
          (isAuthenticatedTask.isSuccessful() &&
              isAuthenticatedTask.getResult().isAuthenticated());

      if (isAuthenticated) {
        // Player authenticated. Continue with Google Play Games Services.
        connectedState = ConnectionState.AUTHENTICATED;
        Log.i(ACTIVITY_TAG, "Player has successfully authenticated.");
        // load saved game
        loginAndLoadSnapshotAsync();
      } else {
        // Authentication failed.
        // Disable integration with Play Games Services.
        // Show a sign-in button allowing players to manually re-attempt sign-in
        // using GamesSignInClient.signIn().
        connectedState = ConnectionState.FAILED;
        Log.i(ACTIVITY_TAG, "Player authentication has failed.");
      }
      notifyConnectionObservers(connectedState);
    };
  }

  /**
   * Displays the user achievements.
   */
  public void showAchievements() {
    if (connectedState != ConnectionState.AUTHENTICATED) {
      // Player is not signed-in.
      Log.d(ACTIVITY_TAG, "Show Achievements Unavailable. User is not authenticated.");
      return;
    }

    PlayGames.getAchievementsClient(mActivity)
        .getAchievementsIntent()
        .addOnSuccessListener(
            intent -> mActivity.startActivityForResult(intent, RC_ACHIEVEMENT_UI));
  }

  /**
   * Is player signed-out? In other words, did the authentication attempt fail? A player can not
   * manually sign-out once authenticated.
   */
  public synchronized boolean isPlayerSignedOut() {
    return connectedState == ConnectionState.FAILED;
  }

  /**
   * Is player signed-in? In other words, did the authentication attempt succeed? A player can not
   * manually sign-out once authenticated.
   */
  public synchronized boolean isPlayerSignedIn() {
    return connectedState == ConnectionState.AUTHENTICATED;
  }

  /*
   * Register an observer for any connection state changes. Normally called
   * when a observer is constructed.
   *
   * Synchronized to avoid adding observer in main thread while notifying
   * connectionObservers in connection callback threads.
   */
  public synchronized void registerConnectionObserver(GooglePlayConnectionObserver observer) {
    Log.d(ACTIVITY_TAG, "Register Google Play Games Service Observer '" + observer + "'.");
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
   * Notify observers of the latest connection state.
   * <p>
   * Synchronized to avoid sending notifications to observers while observers are being
   * added/removed in another thread.
   */
  private synchronized void notifyConnectionObservers(
      ConnectionState connectionState) {
    for (GooglePlayConnectionObserver observer : connectionObservers) {
      Log.i(
          ACTIVITY_TAG,
          "Player sign-in state is: "
              + connectionState.name()
              + ". Sending player sign-in state change to "
              + observer);
      observer.onPlayerSignInStateChange();
    }
  }

  /**
   * Save game progress.
   */
  public void saveGame(final GooglePlaySavedGame savedGame) {

    if (connectedState != ConnectionState.AUTHENTICATED) {
      // Player is not signed-in. Saving game is impossible.
      Log.d(ACTIVITY_TAG, "Save Game Unavailable. User is not authenticated.");
      return;
    }

    // start async save game
    saveSnapshotAsync(savedGame);
  }

  /**
   * Unlock supplied achievements.
   */
  public boolean unlockAchievement(int id) {
    if (connectedState != ConnectionState.AUTHENTICATED) {
      Log.d(ACTIVITY_TAG, "Achievement Unlocks Unavailable. User is not authenticated.");
      return false;
    }

    String achievementId = mActivity.getString(id);
    Log.i(ACTIVITY_TAG, "Achievements: Unlocking id: " + achievementId);
    PlayGames
        .getAchievementsClient(mActivity)
        .unlock(achievementId);
    return true;
  }

  /**
   * Increment supplied achievements.
   */
  public boolean incrementAchievement(int id, int amount) {
    if (connectedState != ConnectionState.AUTHENTICATED) {
      Log.d(ACTIVITY_TAG, "Achievement Increments Unavailable. User is not authenticated.");
      return false;
    }

    String achievementId = mActivity.getString(id);
    Log.i(ACTIVITY_TAG, "Achievements: Incrementing id: " + achievementId + " by " + amount);
    PlayGames
        .getAchievementsClient(mActivity)
        .increment(achievementId, amount);
    return true;
  }

  /**
   * Asynchronously load latest snapshot and resolve conflicts.
   * <p>
   * If current device's game progress is on a higher wave than the the wave stored in Google Play,
   * then immediately overwrite the Google Play's snapshot so other devices can see this progress.
   */
  private void loginAndLoadSnapshotAsync() {

    final SnapshotsClient snapshotsClient =
        PlayGames.getSnapshotsClient(mActivity);

    // attempt to load snapshot from Google Game Play Services
    loadSnapshotAndResolveConflictsTask(snapshotsClient)
        .addOnSuccessListener(snapshot -> {

          // read snapshot
          GooglePlaySavedGame googleSavedGame = extractSavedGame(snapshot);
          int snapshotWaveReached =
              googleSavedGame != null ? googleSavedGame.getHighestWaveReached() : 0;
          Log.i(ACTIVITY_TAG, "Game loaded. Wave: " + snapshotWaveReached);

          // tell SavedGame service of latest saved game loaded.
          // ask for highest wave reached on local device.
          if (savedGame != null) {
            GooglePlaySavedGame deviceSavedGame = savedGame
                .computeHighestWaveOnSavedGameLoaded(googleSavedGame);

            // if player has already progressed beyond snapshot's saved game
            // on local device then update snapshot's save.
            int localWaveReached = deviceSavedGame.getHighestWaveReached();
            if (localWaveReached > snapshotWaveReached) {
              Log.i(ACTIVITY_TAG,
                  "Player has reached wave " + localWaveReached + " on device");
              writeAndCloseSnapshotTask(snapshotsClient, snapshot, deviceSavedGame);
              return;
            }
          }
          // otherwise just close snapshot
          snapshotsClient.discardAndClose(snapshot)
              .addOnSuccessListener(
                  mActivity,
                  task -> Log.i(ACTIVITY_TAG, "Snapshot closed."));
        });
  }

  /**
   * Asynchronously save game progress.
   * <p>
   * Load latest snapshot and resolve conflicts. Then overwrite snapshot with current game progress
   *
   * @param gameToSave - latest game progress to save
   */
  private void saveSnapshotAsync(
      final GooglePlaySavedGame gameToSave) {

    final SnapshotsClient snapshotsClient =
        PlayGames.getSnapshotsClient(mActivity);

    // attempt to load snapshot from Google Game Play Services
    loadSnapshotAndResolveConflictsTask(snapshotsClient)
        .addOnSuccessListener(snapshot -> {

          // read snapshot
          GooglePlaySavedGame googleSavedGame = extractSavedGame(snapshot);
          int snapshotWaveReached =
              googleSavedGame != null ? googleSavedGame.getHighestWaveReached() : 0;
          int savedGameWaveReached = gameToSave.getHighestWaveReached();

          // check for the very rare scenario that another device has progressed
          // beyond the wave we're about to save while we've been playing.
          // in this case, update our saved game service but there's no need
          // for us to save anything to the google play service.
          if (snapshotWaveReached >= savedGameWaveReached) {
            Log.i(ACTIVITY_TAG, "Wave "
                + snapshotWaveReached
                + " already saved to Google Play. Save Cancelled.");
            savedGame.computeHighestWaveOnSavedGameLoaded(googleSavedGame);
            snapshotsClient.discardAndClose(snapshot)
                .addOnSuccessListener(
                    mActivity,
                    task -> Log.i(ACTIVITY_TAG, "Snapshot closed."));
          }

          Log.i(ACTIVITY_TAG,
              "Saving wave " + savedGameWaveReached);
          writeAndCloseSnapshotTask(snapshotsClient, snapshot, gameToSave);
        });
  }

  /**
   * Load the latest snapshot and resolve any conflicts (if any).
   */
  private Task<Snapshot> loadSnapshotAndResolveConflictsTask(SnapshotsClient snapshotsClient) {

    // Open the saved game using its name.
    return snapshotsClient
        .open(SAVED_GAME_FILENAME, true)
        .addOnSuccessListener(
            snapshot -> Log.i(ACTIVITY_TAG, "Successfully opened Snapshot."))
        .addOnFailureListener(
            e -> Log.e(ACTIVITY_TAG, "Error while opening Snapshot.", e))
        .continueWithTask(
            snapshot -> processSnapshotAndResolveConflictsTask(
                snapshotsClient,
                snapshot.getResult(),
                0)
        );
  }

  /**
   * Recursive function that resolves conflicts until snapshot is no longer in conflict. Will
   * eventually give-up if snapshot is still in conflict after a supplied number of re-tries.
   */
  private Task<Snapshot> processSnapshotAndResolveConflictsTask(
      final SnapshotsClient snapshotsClient,
      final SnapshotsClient.DataOrConflict<Snapshot> result,
      final int retryCount) {

    if (!result.isConflict()) {
      Log.i(ACTIVITY_TAG, "Snapshot has no conflicts.");
      // There was no conflict, so return the result of the source.
      TaskCompletionSource<Snapshot> source = new TaskCompletionSource<>();
      source.setResult(result.getData());
      return source.getTask();
    }

    // There was a conflict - we need to resolve it.
    Log.w(ACTIVITY_TAG, "Snapshot conflict found. Attempting to resolve conflict.");
    SnapshotsClient.SnapshotConflict conflict = Objects.requireNonNull(result.getConflict());

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

    return snapshotsClient
        .resolveConflict(conflict.getConflictId(), resolvedSnapshot)
        .continueWithTask(
            task -> {
              // Resolving the conflict may cause another conflict,
              // so recurse and try another resolution.
              if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES) {
                return processSnapshotAndResolveConflictsTask(
                    snapshotsClient,
                    task.getResult(),
                    retryCount + 1);
              } else {
                throw new GalaxyForceException("Could not resolve snapshot conflicts");
              }
            });
  }


  /**
   * Returns a task to write the save game snapshot with a suitable description.
   */
  private void writeAndCloseSnapshotTask(
      SnapshotsClient snapshotsClient,
      Snapshot snapshot,
      GooglePlaySavedGame savedGame) {

    // Set the data payload for the snapshot
    final ObjectMapper mapper = new ObjectMapper();
    final byte[] gameData;
    try {
      gameData = mapper.writeValueAsBytes(savedGame);
    } catch (JsonProcessingException e) {
      Log.w(ACTIVITY_TAG, "Gamed saved failed. Could not create saved game data.", e);
      return;
    }

    snapshot.getSnapshotContents().writeBytes(gameData);
    final int waveUnlocked = savedGame.getHighestWaveReached();
    final String appName = mActivity.getString(R.string.app_name);
    final String description =
        String.format(
            Locale.getDefault(),
            "%s: Unlocked Wave %d",
            appName,
            waveUnlocked);

    // Create the change operation
    SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
        .setDescription(description)
        .build();

    // Commit the operation
    snapshotsClient.commitAndClose(snapshot, metadataChange)
        .addOnSuccessListener(
            mActivity,
            snapshotMetadata -> Log.i(ACTIVITY_TAG,
                "Gamed saved and snapshot closed. Wave: " + waveUnlocked));
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
}
