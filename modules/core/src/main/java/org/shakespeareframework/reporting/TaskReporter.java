package org.shakespeareframework.reporting;

import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.RetryableTask;
import org.shakespeareframework.Task;

/**
 * A {@link TaskReporter} to be {@link Actor#informs informed} by {@link Actor}s about their
 * actions.
 */
public interface TaskReporter {

  /**
   * Reports the start of doing the given {@link Task}.
   *
   * @param actor the acting {@link Actor}
   * @param task the started {@link Task}
   */
  default void start(Actor actor, Task task) {}

  /**
   * Reports the retry of the latest started {@link RetryableTask}.
   *
   * @param actor the acting {@link Actor}
   * @param task the retried {@link Task}
   * @param cause the cause for the retry
   */
  default void retry(Actor actor, RetryableTask task, Exception cause) {}

  /**
   * Reports the successful finishing of the latest started {@link Task}.
   *
   * @param actor the acting {@link Actor}
   * @param task the successfully finished {@link Task}
   */
  default void success(Actor actor, Task task) {}

  /**
   * Reports the unsuccessful finishing of the latest started {@link Task} or {@link Question}.
   *
   * @param actor the acting {@link Actor}
   * @param task the unsuccessfully finished {@link Task}
   * @param cause the cause of the failure
   */
  default void failure(Actor actor, Task task, Exception cause) {}
}
