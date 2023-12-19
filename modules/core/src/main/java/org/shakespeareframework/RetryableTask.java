package org.shakespeareframework;

import java.util.Set;

/**
 * A {@link Task} that will be retried until it succeeds or the {@link Retryable#getTimeout()
 * timeout} is reached. {@link Exception}s thrown will be ignored by default unless they match
 * {@link #isAcknowledgedException(Exception) the acknowledged exception predicate}.
 */
public interface RetryableTask extends Task, Retryable {

  /**
   * @return the {@link Set} of {@link Exception} classes that won't be ignored when thrown in a
   *     retry
   */
  default Set<Class<? extends Exception>> getAcknowledgedExceptions() {
    return Set.of();
  }

  /**
   * @return the predicate that decides weather the exception won't be ignored when thrown in a
   *     retry
   */
  default boolean isAcknowledgedException(Exception exception) {
    return getAcknowledgedExceptions().stream()
        .anyMatch(acknowledge -> acknowledge.isInstance(exception));
  }
}
