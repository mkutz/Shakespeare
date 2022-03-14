package org.shakespeareframework.reporting;

import org.shakespeareframework.Question;
import org.shakespeareframework.Task;

import java.time.Duration;
import java.time.Instant;

import static java.time.Instant.now;

/**
 * Report on a {@link Task} or {@link Question}.
 *
 * @param <T> the Type of the {@link #subject}.
 */
abstract class Report<T> {

    protected final String actorName;
    protected final T subject;
    protected final Instant started;
    protected Duration duration;
    protected Status status = Status.STARTED;
    protected int retries = 0;
    protected Exception cause;

    /**
     * @param actorName the {@link org.shakespeareframework.Actor}'s name
     * @param subject   the {@link Question} this is about
     */
    Report(String actorName, T subject) {
        this.actorName = actorName;
        this.subject = subject;
        this.started = now();
    }

    /**
     * Increments the {@link #retries} counter.
     */
    void retry() {
        retries++;
    }

    /**
     * Marks the report as finished successfully.
     *
     * @return this {@link Report}
     */
    Report<T> success() {
        status = Status.SUCCESS;
        duration = Duration.between(started, now());
        return this;
    }

    /**
     * Marks the report as finished unsuccessfully.
     *
     * @param cause the {@link Exception} causing the failure.
     * @return this {@link Report}
     */
    Report<T> failure(Exception cause) {
        status = Status.FAILURE;
        duration = Duration.between(started, now());
        this.cause = cause;
        return this;
    }

    /**
     * @return the {@link Duration} since the {@link Report} was created until either now if the {@link #status} is not
     * finished yet, or the instant the report was finished.
     */
    Duration getDuration() {
        return duration == null ? Duration.between(started, now()) : duration;
    }

    /**
     * @return the String representation of this {@link Report}.
     */
    public abstract String asString();

    @Override
    public String toString() {
        return asString();
    }

    protected enum Status {

        STARTED('…'), SUCCESS('✓'), FAILURE('✗');

        final char symbol;

        Status(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }
}
