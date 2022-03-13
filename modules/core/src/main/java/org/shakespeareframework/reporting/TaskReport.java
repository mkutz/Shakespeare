package org.shakespeareframework.reporting;

import org.shakespeareframework.Task;

import static java.lang.String.format;

class TaskReport extends Report<Task> {

    TaskReport(String actorName, Task subject) {
        super(actorName, subject);
    }

    @Override
    public String asString() {
        return format("%s does %s %s %s%c%s",
                actorName,
                subject,
                DurationFormatter.format(getDuration()),
                "•".repeat(retries),
                status.symbol,
                cause != null ? " " + cause.getClass().getSimpleName() : "");
    }
}
