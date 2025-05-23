package org.shakespeareframework;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.testing.TestTaskBuilder;

class ActorDoesRetryableTest {

  Actor actor = new Actor();

  @Test
  @DisplayName("does calls the task's performAs until its timeout")
  void doesRetryableTest1() {
    var called = new AtomicInteger(0);
    var retryableTask =
        new TestTaskBuilder()
            .timeout(ofMillis(100))
            .interval(ofMillis(10))
            .perform(
                they -> {
                  called.incrementAndGet();
                  throw new RuntimeException();
                })
            .buildRetryable();

    assertThatExceptionOfType(TimeoutException.class).isThrownBy(() -> actor.does(retryableTask));

    assertThat(called).hasValueBetween(9, 11);
  }

  @Test
  @DisplayName("does returns immediately after success")
  void doesRetryableTest2() {
    var called = new AtomicInteger(0);
    var retryableTask =
        new TestTaskBuilder().perform(they -> called.incrementAndGet()).buildRetryable();

    actor.does(retryableTask);

    assertThat(called).hasValue(1);
  }

  @Test
  @DisplayName("does throws acknowledged exceptions immediately")
  void doesRetryableTest3() {
    var called = new AtomicInteger(0);
    var retryableTask =
        new TestTaskBuilder()
            .acknowledgedExceptions(IllegalStateException.class)
            .perform(
                they -> {
                  called.incrementAndGet();
                  throw new IllegalStateException();
                })
            .buildRetryable();

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> actor.does(retryableTask));

    assertThat(called).hasValue(1);
  }
}
