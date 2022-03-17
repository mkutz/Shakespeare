package org.shakespeareframework;

import java.time.Duration;
import java.util.Set;
import java.util.function.Function;

import static java.time.Duration.ofMillis;

public final class RetryableTestQuestionBuilder<A> extends TestQuestionBuilder<A> {

    private Duration timeout = ofMillis(100);
    private Duration interval = ofMillis(10);
    private Set<Class<? extends Exception>> ignoredExceptions = Set.of();
    private Function<A, Boolean> acceptable = (A) -> true;

    public RetryableTestQuestionBuilder<A> timeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public RetryableTestQuestionBuilder<A> interval(Duration interval) {
        this.interval = interval;
        return this;
    }

    public RetryableTestQuestionBuilder<A> ignoredExceptions(Set<Class<? extends Exception>> ignoredExceptions) {
        this.ignoredExceptions = ignoredExceptions;
        return this;
    }

    public RetryableTestQuestionBuilder<A> acceptable(Function<A, Boolean> acceptable) {
        this.acceptable = acceptable;
        return this;
    }

    @Override
    public RetryableTestQuestionBuilder<A> string(String string) {
        super.string(string);
        return this;
    }

    @Override
    public RetryableQuestion<A> answer(Function<Actor, A> answer) {
        return new RetryableQuestion<>() {

            @Override
            public A answerAs(Actor actor) {
                return answer.apply(actor);
            }

            @Override
            public boolean acceptable(A answer) {
                return acceptable.apply(answer);
            }

            @Override
            public Duration getTimeout() {
                return timeout;
            }

            @Override
            public Duration getInterval() {
                return interval;
            }

            @Override
            public Set<Class<? extends Exception>> getIgnoredExceptions() {
                return ignoredExceptions;
            }

            @Override
            public String toString() {
                return string;
            }
        };
    }
}
