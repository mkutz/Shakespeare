package org.shakespeareframework;

import java.util.Set;

/**
 * A {@link Task} that will be retried until it succeeds or the {@link Retryable#getTimeout() timeout} is reached.
 * {@link Exception}s thrown will be ignored by default unless they are contained in the
 * {@link #getAcknowledgedExceptions() acknowledgedExceptions}.
 */
public interface RetryableTask extends Task, Retryable {

    /**
     * @return the {@link Set} of {@link Exception} classes that won't be ignored when thrown in a retry
     */
    default Set<Class<? extends Exception>> getAcknowledgedExceptions() {
        return Set.of();
    }
}
