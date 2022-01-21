package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActorAnswersRetryableTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("answers calls the question's answeredAs until its timeout")
    void answersRetryableTest1() {
        final var called = new AtomicInteger(0);
        final var retryableQuestion = new RetryableTestQuestionBuilder<Integer>()
                .timeout(ofMillis(100))
                .interval(ofMillis(10))
                .acceptable((ignored) -> false)
                .answer(actor -> called.incrementAndGet());

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checks(retryableQuestion));

        assertThat(called).hasValueGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("answers returns acceptable answers immediately")
    void answersRetryableTest2() {
        final var answer = "Answer";
        final var retryableQuestion = new RetryableTestQuestionBuilder<String>()
                .answer(actor -> answer);

        assertThat(actor.checks(retryableQuestion)).isEqualTo(answer);
    }

    @Test
    @DisplayName("answers catches ignored exceptions immediately")
    void answersRetryableTest3() {
        final var called = new AtomicInteger(0);
        final var retryableQuestion = new RetryableTestQuestionBuilder<Integer>()
                .timeout(ofMillis(100))
                .interval(ofMillis(10))
                .ignoredExceptions(Set.of(IllegalStateException.class))
                .answer(actor -> {
                    called.incrementAndGet();
                    throw new IllegalStateException();
                });

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checks(retryableQuestion))
                .withCauseExactlyInstanceOf(IllegalStateException.class);

        assertThat(called).hasValueGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("answers throws not ignored exceptions immediately")
    void answersRetryableTest4() {
        final var called = new AtomicInteger(0);
        final var retryableQuestion = new RetryableTestQuestionBuilder<Integer>()
                .ignoredExceptions(Set.of())
                .answer(actor -> {
                    called.incrementAndGet();
                    throw new IllegalStateException();
                });

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> actor.checks(retryableQuestion));

        assertThat(called).hasValue(1);
    }

    private static final class RetryableTestQuestionBuilder<A> {

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
            };
        }
    }

}
