package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActorDoesRetryableTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("does calls the task's performAs until its timeout")
    void doesRetryableTest1() {
        final var called = new AtomicInteger(0);
        final var retryableTask = new RetryableTestTaskBuilder()
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
        final var called = new AtomicInteger(0);
        final var retryableTask = new RetryableTestTaskBuilder()
                .perform(actor -> called.incrementAndGet());

        actor.does(retryableTask);

        assertThat(called).hasValue(1);
    }

    @Test
    @DisplayName("does throws acknowledged exceptions immediately")
    void doesRetryableTest3() {
        final var called = new AtomicInteger(0);
        final var retryableTask = new RetryableTestTaskBuilder()
                .acknowledgedExceptions(Set.of(IllegalStateException.class))
                .perform(actor -> {
                    called.incrementAndGet();
                    throw new IllegalStateException();
                });

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> actor.does(retryableTask));

        assertThat(called).hasValue(1);
    }

    private static final class RetryableTestTaskBuilder {

        private Duration timeout = ofMillis(100);
        private Duration interval = ofMillis(10);
        private Set<Class<? extends Exception>> acknowledgedExceptions = Set.of();

        public RetryableTestTaskBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public RetryableTestTaskBuilder interval(Duration interval) {
            this.interval = interval;
            return this;
        }

        public RetryableTestTaskBuilder acknowledgedExceptions(Set<Class<? extends Exception>> acknowledgedExceptions) {
            this.acknowledgedExceptions = acknowledgedExceptions;
            return this;
        }

        public RetryableTask perform(Consumer<Actor> perform) {
            return new RetryableTask() {

                @Override
                public void performAs(Actor actor) {
                    perform.accept(actor);
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
                public Set<Class<? extends Exception>> getAcknowledgedExceptions() {
                    return acknowledgedExceptions;
                }
            };
        }
    }
}
