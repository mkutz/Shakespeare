package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.testing.TestQuestionBuilder;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActorAnswersRetryableTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("answers calls the question's answeredAs until its timeout")
    void answersRetryableTest1() {
        var called = new AtomicInteger(0);
        var retryableQuestion = new TestQuestionBuilder<Integer>()
                .timeout(ofMillis(100))
                .interval(ofMillis(10))
                .acceptable((ignored) -> false)
                .answer(actor -> called.incrementAndGet())
                .buildRetryable();

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checks(retryableQuestion));

        assertThat(called).hasValueGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("answers returns acceptable answers immediately")
    void answersRetryableTest2() {
        var answer = "Answer";
        var retryableQuestion = new TestQuestionBuilder<String>()
                .answer(actor -> answer)
                .buildRetryable();

        assertThat(actor.checks(retryableQuestion)).isEqualTo(answer);
    }

    @Test
    @DisplayName("answers catches ignored exceptions immediately")
    void answersRetryableTest3() {
        var called = new AtomicInteger(0);
        var retryableQuestion = new TestQuestionBuilder<Integer>()
                .timeout(ofMillis(100))
                .interval(ofMillis(10))
                .ignoredExceptions(IllegalStateException.class)
                .answer(actor -> {
                    called.incrementAndGet();
                    throw new IllegalStateException();
                })
                .buildRetryable();

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checks(retryableQuestion))
                .withCauseExactlyInstanceOf(IllegalStateException.class);

        assertThat(called).hasValueGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("answers throws not ignored exceptions immediately")
    void answersRetryableTest4() {
        var called = new AtomicInteger(0);
        var retryableQuestion = new TestQuestionBuilder<Integer>()
                .ignoredExceptions()
                .answer(actor -> {
                    called.incrementAndGet();
                    throw new IllegalStateException();
                })
                .buildRetryable();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> actor.checks(retryableQuestion));

        assertThat(called).hasValue(1);
    }

}
