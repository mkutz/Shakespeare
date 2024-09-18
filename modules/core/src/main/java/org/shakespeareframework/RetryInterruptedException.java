package org.shakespeareframework;

import static java.lang.String.format;

import org.jspecify.annotations.NullMarked;

/**
 * A {@link RetryInterruptedException} is thrown when an {@link Actor} get interrupted while
 * retrying a {@link Retryable}.
 *
 * @see Actor#does(RetryableTask)
 * @see Actor#checks(RetryableQuestion)
 */
@NullMarked
public class RetryInterruptedException extends RuntimeException {

  /**
   * @param actor the interrupted {@link Actor}
   * @param retryable the {@link Retryable}
   * @param cause the {@link InterruptedException} which caused the interruption
   */
  public RetryInterruptedException(Actor actor, Retryable retryable, InterruptedException cause) {
    super(format("%s was interrupted while retrying %s", actor, retryable), cause);
  }
}
