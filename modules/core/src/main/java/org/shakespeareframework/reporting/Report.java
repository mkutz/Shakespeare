package org.shakespeareframework.reporting;

import org.shakespeareframework.Question;
import org.shakespeareframework.Task;

import java.time.Duration;
import java.time.Instant;

import static java.lang.String.format;
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

    Report(String actorName, T subject) {
        this.actorName = actorName;
        this.subject = subject;
        this.started = now();
    }

    void retry() {
        retries++;
    }

    Report<T> success() {
        status = Status.SUCCESS;
        duration = Duration.between(started, now());
        return this;
    }

    Report<T> failure(Exception cause) {
        status = Status.FAILURE;
        duration = Duration.between(started, now());
        this.cause = cause;
        return this;
    }

    Duration getDuration() {
        return duration == null ? Duration.between(started, now()) : duration;
    }

    public abstract String asString();

    @Override
    public String toString() {
        return asString();
    }

    enum Status {

        STARTED('…'), SUCCESS('✓'), FAILURE('✗');

        final char symbol;

        Status(char symbol) {
            this.symbol = symbol;
        }
    }
}
