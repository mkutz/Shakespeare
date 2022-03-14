package org.shakespeareframework.reporting;

import org.shakespeareframework.*;

/**
 * A {@link Reporter} to be {@link Actor#informs informed} by {@link Actor}s about their actions.
 */
public interface Reporter {

    /**
     * Reports the start of doing the given {@link Task}.
     *
     * @param actor the acting {@link Actor}
     * @param task the started {@link Task}
     */
    default void start(Actor actor, Task task) {}

    /**
     * Reports the start of checking the given {@link Question}.
     *
     * @param actor the acting {@link Actor}
     * @param question the started {@link Question}
     */
    default void start(Actor actor, Question<?> question) {}

    /**
     * Reports the retry of the latest started {@link Retryable}.
     *
     * @param actor the acting {@link Actor}
     * @param cause the cause for the retry
     */
    default void retry(Actor actor, Exception cause) {}

    /**
     * Reports the retry of the latest started {@link RetryableQuestion}.
     *
     * @param actor the acting {@link Actor}
     * @param answer the current unacceptable answer
     */
    default void retry(Actor actor, Object answer) {}

    /**
     * Reports the successful finishing of the latest started {@link Task}.
     *
     * @param actor the acting {@link Actor}
     */
    default void success(Actor actor) {}

    /**
     * Reports the successful finishing of the lastest started {@link Question}.
     *
     * @param actor the acting {@link Actor}
     * @param answer the found answer
     * @param <A> the type of the given answer.
     */
    default <A> void success(Actor actor, A answer) {}

    /**
     * Reports the unsuccessful finishing of the latest started {@link Task} or {@link Question}.
     *
     * @param actor the acting {@link Actor}
     * @param cause the cause of the failure
     */
    default void failure(Actor actor, Exception cause) {}
}
