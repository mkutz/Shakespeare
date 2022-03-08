package org.shakespeareframework;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;

import static java.time.Duration.ofMillis;

final class RetryableTestTaskBuilder {

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
