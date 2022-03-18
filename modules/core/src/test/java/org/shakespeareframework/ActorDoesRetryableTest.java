package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActorDoesRetryableTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("does calls the task's performAs until its timeout")
    void doesRetryableTest1() {
        var called = new AtomicInteger(0);
        var retryableTask = new RetryableTestTaskBuilder()
                .timeout(ofMillis(100))
                .interval(ofMillis(10))
                .perform(actor -> {
                    called.incrementAndGet();
                    throw new RuntimeException();
                });

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.does(retryableTask));

        assertThat(called).hasValueGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("does returns immediately after success")
    void doesRetryableTest2() {
        var called = new AtomicInteger(0);
        var retryableTask = new RetryableTestTaskBuilder()
                .perform(actor -> called.incrementAndGet());

        actor.does(retryableTask);

        assertThat(called).hasValue(1);
    }

    @Test
    @DisplayName("does throws acknowledged exceptions immediately")
    void doesRetryableTest3() {
        var called = new AtomicInteger(0);
        var retryableTask = new RetryableTestTaskBuilder()
                .acknowledgedExceptions(Set.of(IllegalStateException.class))
                .perform(actor -> {
                    called.incrementAndGet();
                    throw new IllegalStateException();
                });

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> actor.does(retryableTask));

        assertThat(called).hasValue(1);
    }

}
