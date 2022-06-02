package com.danosoftware.galaxyforce.billing;

import android.util.Log;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Responsible for queueing billing tasks while the billing client is connecting.
 * <p>
 * Once notified of the client connection, any queued tasks will be run immediately.
 */
public class BillingClientQueue {

  private static final String TAG = "BillingClientQueue";

  private final Queue<Runnable> delayedTasks;
  private boolean tasksReleased;

  public BillingClientQueue() {
    this.delayedTasks = new ArrayDeque<>();
    this.tasksReleased = false;
  }

  /**
   * Add a new runnable task to the client queue.
   * <p>
   * This method is called when a client is not yet ready to run our wanted task.
   * <p>
   * Method is synchronized to prevent different threads calling delay() and releaseTasks()
   * concurrently.
   *
   * @param task - task to be run when client is
   */
  public synchronized void delay(Runnable task) {
    // in rare race conditions, it may be possible that the client connected
    // after delay() was called. To avoid this task being stuck in the queue
    // forever, we first check if the task can run immediately.
    if (tasksReleased) {
      task.run();
    } else {
      Log.d(TAG, "Task will be queued...");
      delayedTasks.offer(task);
    }
  }

  /**
   * Client has connected. Execute all queued tasks.
   */
  public synchronized void releaseTasks() {

    if (delayedTasks.size() > 0) {
      Log.d(TAG, delayedTasks.size() + " queued task/s will be executed...");
    }
    for (Runnable task : delayedTasks) {
      task.run();
    }
    tasksReleased = true;
  }

  /**
   * Client has disconnected.
   * <p>
   * Queue any new tasks received until it connects again.
   */
  public synchronized void delayTasks() {
    tasksReleased = false;
  }
}
