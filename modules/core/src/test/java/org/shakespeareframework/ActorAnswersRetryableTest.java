package org.shakespeareframework;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.data.Offset.offset;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.testing.TestQuestionBuilder;

class ActorAnswersRetryableTest {

  Actor actor = new Actor();

  @Test
  @DisplayName("answers calls the question's answeredAs until its timeout")
  void answersRetryableTest1() {
    var called = new AtomicInteger(0);
    var retryableQuestion =
        new TestQuestionBuilder<Integer>()
            .timeout(ofMillis(100))
            .interval(ofMillis(10))
            .acceptable((ignored) -> false)
            .answer(actor -> called.incrementAndGet())
            .buildRetryable();

    assertThatExceptionOfType(TimeoutException.class)
        .isThrownBy(() -> actor.checks(retryableQuestion));

    assertThat(called).hasValueBetween(9, 11);
  }

  @Test
  @DisplayName("answers returns acceptable answers immediately")
  void answersRetryableTest2() {
    var answer = "Answer";
    var retryableQuestion =
        new TestQuestionBuilder<String>().answer(actor -> answer).buildRetryable();

    assertThat(actor.checks(retryableQuestion)).isEqualTo(answer);
  }

  @Test
  @DisplayName("answers catches ignored exceptions immediately")
  void answersRetryableTest3() {
    var called = new AtomicInteger(0);
    var retryableQuestion =
        new TestQuestionBuilder<Integer>()
            .timeout(ofMillis(100))
            .interval(ofMillis(10))
            .ignoredExceptions(IllegalStateException.class)
            .answer(
                actor -> {
                  called.incrementAndGet();
                  throw new IllegalStateException();
                })
            .buildRetryable();

    assertThatExceptionOfType(TimeoutException.class)
        .isThrownBy(() -> actor.checks(retryableQuestion))
        .withCauseExactlyInstanceOf(IllegalStateException.class);

    assertThat(called).hasValueCloseTo(10, offset(1));
  }

  @Test
  @DisplayName("answers throws not ignored exceptions immediately")
  void answersRetryableTest4() {
    var called = new AtomicInteger(0);
    var retryableQuestion =
        new TestQuestionBuilder<Integer>()
            .ignoredExceptions()
            .answer(
                actor -> {
                  called.incrementAndGet();
                  throw new IllegalStateException();
                })
            .buildRetryable();

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> actor.checks(retryableQuestion));

    assertThat(called).hasValue(1);
  }
}
