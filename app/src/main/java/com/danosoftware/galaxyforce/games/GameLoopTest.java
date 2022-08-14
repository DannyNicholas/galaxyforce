package com.danosoftware.galaxyforce.games;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.googleplay.GooglePlayServices;
import com.danosoftware.galaxyforce.tasks.TaskService;
import com.danosoftware.galaxyforce.view.GLGraphics;
import java.util.ArrayList;
import java.util.List;
import lombok.Value;

/**
 * Runs a game loop test that cycles through different sections of the game automatically. Used for
 * benchmarking and performance reports.
 */
public class GameLoopTest extends GameImpl {

  private final boolean hasSwitched = false;
  private float timeElasped = 0f;
  private final Activity activity;
  private final List<ActionItem> sequence;
  private int nextSequence;

  public GameLoopTest(
      Context context,
      GLGraphics glGraphics,
      GLSurfaceView glView,
      BillingService billingService,
      GooglePlayServices playService,
      ConfigurationService configurationService,
      TaskService taskService) {

    super(context, glGraphics, glView, billingService, playService, configurationService,
        taskService);
    this.activity = (Activity) context;
    this.sequence = createSequences();
    this.nextSequence = 0;
  }

  // create a sequence of actions for the game loop
  private List<ActionItem> createSequences() {
    List<ActionItem> sequences = new ArrayList<>();
    // allow game time to display splash screen and progress to main menu
    // select options screen and back
    sequences.add(new ActionItem(10f, () -> this.changeToReturningScreen(ScreenType.OPTIONS)));
    sequences.add(new ActionItem(15f, this::handleBackButton));

    // select upgrade screen and back
    sequences.add(
        new ActionItem(20f, () -> this.changeToReturningScreen(ScreenType.UPGRADE_FULL_VERSION)));
    sequences.add(new ActionItem(25f, this::handleBackButton));

    // select level screen and back
    sequences.add(new ActionItem(30f, () -> this.changeToScreen(ScreenType.SELECT_LEVEL)));
    sequences.add(new ActionItem(35f, this::handleBackButton));

    // select level screen and start game
    sequences.add(new ActionItem(40f, () -> this.changeToScreen(ScreenType.SELECT_LEVEL)));
    sequences.add(new ActionItem(45f, () -> this.changeToGameScreen(1)));

    // restart game every 30 seconds
    sequences.add(new ActionItem(30f * 2, () -> this.changeToGameScreen(1)));
    sequences.add(new ActionItem(30f * 3, () -> this.changeToGameScreen(1)));
    sequences.add(new ActionItem(30f * 4, () -> this.changeToGameScreen(1)));
    sequences.add(new ActionItem(30f * 5, () -> this.changeToGameScreen(1)));
    sequences.add(new ActionItem(30f * 6, () -> this.changeToGameScreen(1)));

    // exit game immediately
    sequences.add(new ActionItem((30f * 6) + 2f, this::handleBackButton)); // back to pause
    sequences.add(new ActionItem((30f * 6) + 3f, this::handleBackButton)); // back to select level
    sequences.add(new ActionItem((30f * 6) + 4f, this::handleBackButton)); // back to main menu
    sequences.add(new ActionItem((30f * 6) + 5f, this::handleBackButton)); // exit main menu
    sequences.add(new ActionItem((30f * 6) + 6f, activity::finish));
    return sequences;
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    timeElasped += deltaTime;

    if (nextSequence < sequence.size()) {
      ActionItem actionItem = sequence.get(nextSequence);
      if (timeElasped > actionItem.getTime()) {
        actionItem.getAction().run();
        nextSequence++;
      }
    }
  }

  // represents an action to perform at a set time duration
  @Value
  private static class ActionItem {

    Float time;
    Runnable action;
  }
}
