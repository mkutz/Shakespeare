package org.shakespeareframework.reporting;

import org.shakespeareframework.Task;

import static java.lang.String.format;

/**
 * {@link Report} on a {@link Task}.
 */
class TaskReport extends Report<Task> {

    /**
     * @param actorName the {@link org.shakespeareframework.Actor}'s name
     * @param subject   the {@link Task} this is about
     */
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
                status.getSymbol(),
                cause != null ? " " + cause.getClass().getSimpleName() : "");
    }
}
