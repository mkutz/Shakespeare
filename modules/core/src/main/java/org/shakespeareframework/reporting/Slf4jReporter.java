package org.shakespeareframework.reporting;

import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.Task;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class Slf4jReporter implements LoggingReporter {

    private LoggingReport currentRootReport;

    @Override
    public void start(Actor actor, Task task) {
        final var report = new LoggingReport(format("%s does %s", actor.getName(), task));
        if (currentRootReport == null) currentRootReport = report;
        else currentRootReport.addSubReport(report);
    }

    @Override
    public void start(Actor actor, Question<?> question) {
        final var report = new LoggingReport(format("%s does %s", actor.getName(), question));
        if (currentRootReport == null) currentRootReport = report;
        else currentRootReport.addSubReport(report);
    }

    @Override
    public void retry(Actor actor, Exception cause) {
        getCurrentRootReport().retry();
    }

    @Override
    public void retry(Actor actor, Object answer) {
        getCurrentRootReport().retry();
    }

    @Override
    public void success(Actor actor) {
        final var logger = getLogger(actor.getName());
        if (logger.isInfoEnabled()) {
            logger.info(getCurrentRootReport().success().toString());
        }
    }

    @Override
    public <A> void success(Actor actor, A answer) {
        final var logger = getLogger(actor.getName());
        if (logger.isInfoEnabled()) {
            logger.info(getCurrentRootReport().success(format("→ %s", answer)).toString());
        }
    }

    @Override
    public void failure(Actor actor, Exception cause) {
        final var logger = getLogger(actor.getName());
        if (logger.isWarnEnabled()) {
            logger.warn(getCurrentRootReport().failure(cause.getClass().getSimpleName()).toString());
        }
    }

    private LoggingReport getCurrentRootReport() {
        if (currentRootReport == null) {
            throw new IllegalStateException("There is not current report");
        }
        return currentRootReport.getCurrentReport();
    }
}
