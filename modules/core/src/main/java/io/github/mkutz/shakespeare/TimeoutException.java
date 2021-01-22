package io.github.mkutz.shakespeare;

/**
 * A {@link TimeoutException} is thrown in case an {@link Actor} does not succeed on a {@link Retryable} before its
 * timeout is reached.
 */
public class TimeoutException extends RuntimeException {

    /**
     * @param actor         the {@link Actor} ran into the timeout
     * @param retryable     the {@link Retryable} which was not successful before its timeout
     * @param lastException the exception that was thrown in the last retry
     */
    public TimeoutException(Actor actor, Retryable retryable, Throwable lastException) {
        super(String.format("%s failed to %s before its timeout of %s",
                actor, retryable, retryable.getTimeout()), lastException);
    }

    /**
     * @param actor     the {@link Actor} ran into the timeout
     * @param retryable the {@link Retryable} which was not successful before its timeout
     */
    public TimeoutException(Actor actor, Retryable retryable) {
        this(actor, retryable, null);
    }
}
