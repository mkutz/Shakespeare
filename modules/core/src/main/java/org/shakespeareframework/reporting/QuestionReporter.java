package org.shakespeareframework.reporting;

import org.shakespeareframework.*;

/**
 * A {@link QuestionReporter} to be {@link Actor#informs informed} by {@link Actor}s about their actions.
 */
public interface QuestionReporter {

    /**
     * Reports the start of checking the given {@link Question}.
     *
     * @param actor the acting {@link Actor}
     * @param question the started {@link Question}
     */
    default void start(Actor actor, Question<?> question) {}

    /**
     * Reports the retry of the latest started {@link RetryableQuestion} due to an ignored exception.
     *
     * @param actor the acting {@link Actor}
     * @param question the retried question
     * @param cause the cause for the retry
     */
    default void retry(Actor actor, RetryableQuestion<?> question, Exception cause) {}

    /**
     * Reports the retry of the latest started {@link RetryableQuestion} due to an unacceptable answer.
     *
     * @param actor the acting {@link Actor}
     * @param question the retried question
     * @param answer the current unacceptable answer
     */
    default <A> void retry(Actor actor, RetryableQuestion<A> question, A answer) {}

    /**
     * Reports the successful finishing of the lastest started {@link Question}.
     *
     * @param actor the acting {@link Actor}
     * @param question the successfully finished {@link Question}
     * @param answer the found answer
     * @param <A> the type of the given answer.
     */
    default <A> void success(Actor actor, Question<A> question, A answer) {}

    /**
     * Reports the unsuccessful finishing of the latest started {@link Question} due to an acknowledged exception.
     *
     * @param actor the acting {@link Actor}
     * @param question the unsuccessfully finished {@link Question}
     * @param cause the cause of the failure
     */
    default void failure(Actor actor, Question<?> question, Exception cause) {}

    /**
     * Reports the unsuccessful finishing of the latest started {@link RetryableQuestion} due to an unacceptable answer.
     *
     * @param actor the acting {@link Actor}
     * @param answer the found answer
     */
    default <A> void failure(Actor actor, Question<A> question, A answer) {}
}
