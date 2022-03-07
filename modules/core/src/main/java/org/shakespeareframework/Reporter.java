package org.shakespeareframework;

/**
 * A {@link Reporter} to be {@link Actor#informs informed} by {@link Actor}s about their actions.
 */
public interface Reporter {

    /**
     * Reports the start of doing the given {@link Task}.
     *
     * @param task the started {@link Task}
     */
    default void start(Task task) {
    }

    /**
     * Reports the start of checking the given {@link Question}.
     *
     * @param question the started {@link Question}
     */
    default void start(Question<?> question) {
    }

    /**
     * Reports the retry of the latest started {@link Retryable}.
     *
     * @param cause the cause for the retry.
     */
    default void retry(Exception cause) {
    }

    /**
     * Reports the successful finishing of the latest started {@link Task}.
     */
    default void success() {
    }

    /**
     * Reports the successful finishing of the lastest started {@link Question}.
     *
     * @param answer the found answer.
     */
    default void success(Object answer) {
    }

    /**
     * Reports the unsuccessful finishing of the latest started {@link Task} or {@link Question}.
     */
    default void failure() {
    }

    /**
     * Reports the unsuccessful finishing of the latest started {@link Task} or {@link Question}.
     *
     * @param cause the cause of the failure.
     */
    default void failure(Exception cause) {
    }
}
