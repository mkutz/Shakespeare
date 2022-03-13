package org.shakespeareframework.reporting;

import org.shakespeareframework.Question;

import java.time.Duration;

import static java.lang.String.format;
import static java.time.Instant.now;

class QuestionReport extends Report<Question<?>> {

    private Object answer;

    QuestionReport(String actorName, Question<?> subject) {
        super(actorName, subject);
    }

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
                status.symbol,
                cause != null ? " " + cause.getClass().getSimpleName() : "",
                answer != null ? "\n→ " + answer : "");
    }
}
