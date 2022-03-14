package org.shakespeareframework.reporting;

import org.shakespeareframework.Question;

import java.time.Duration;

import static java.lang.String.format;
import static java.time.Instant.now;

/**
 * {@link Report} on a {@link Question}.
 */
class QuestionReport extends Report<Question<?>> {

    /**
     * The answer to the {@link #subject} {@link Question}.
     */
    private Object answer;

    /**
     * @param actorName the {@link org.shakespeareframework.Actor}'s name
     * @param subject   the {@link Question} this is about
     */
    QuestionReport(String actorName, Question<?> subject) {
        super(actorName, subject);
    }

    /**
     * @param answer the answer to the {@link #subject} {@link Question}
     * @return the {@link QuestionReport}.
     */
    QuestionReport success(Object answer) {
        status = Status.SUCCESS;
        duration = Duration.between(started, now());
        this.answer = answer;
        return this;
    }

    @Override
    public String asString() {
        return format("%s checks %s %s %s%c%s%s",
                actorName,
                subject,
                DurationFormatter.format(getDuration()),
                "•".repeat(retries),
                status.getSymbol(),
                cause != null ? " " + cause.getClass().getSimpleName() : "",
                answer != null ? "\n→ " + answer : "");
    }
}
