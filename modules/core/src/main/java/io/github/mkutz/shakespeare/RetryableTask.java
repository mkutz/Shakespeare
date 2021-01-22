package io.github.mkutz.shakespeare;

import java.util.Set;

/**
 * A {@link Task} that will be retried until it succeeds or the {@link #getTimeout()} is reached. {@link Exception}s
 * thrown will be ignored by default unless they are contained in {@link #getAcknowledgedExceptions()}.
 */
public interface RetryableTask extends Task, Retryable {

    /**
     * @return the {@link Set} of {@link Throwable} classes that won't be ignored when thrown in a retry
     */
    Set<Class<? extends Throwable>> getAcknowledgedExceptions();
}
