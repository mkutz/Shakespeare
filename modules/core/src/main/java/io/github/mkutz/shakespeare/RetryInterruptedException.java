package io.github.mkutz.shakespeare;

/**
 * A {@link RetryInterruptedException} is thrown when an {@link Actor} get interrupted while retrying a
 * {@link Retryable}.
 *
 * @see Actor#doesEventually(RetryableTask)
 * @see Actor#checksEventually(RetryableQuestion)
 */
public class RetryInterruptedException extends RuntimeException {

    /**
     * @param actor     the interrupted {@link Actor}
     * @param retryable the {@link Retryable}
     * @param cause     the {@link InterruptedException} which caused the interruption
     */
    public RetryInterruptedException(Actor actor, Retryable retryable, InterruptedException cause) {
        super("%s was interrupted while retrying %s".formatted(actor, retryable), cause);
    }
}
