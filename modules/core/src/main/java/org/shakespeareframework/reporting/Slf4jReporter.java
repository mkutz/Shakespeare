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
        final var report = new LoggingReport(format("%s checks %s", actor.getName(), question));
        if (currentRootReport == null) currentRootReport = report;
        else currentRootReport.addSubReport(report);
    }

    @Override
    public void retry(Actor actor, Exception cause) {
        currentRootReport.getCurrentReport().retry();
    }

    @Override
    public void retry(Actor actor, Object answer) {
        currentRootReport.getCurrentReport().retry();
    }

    @Override
    public void success(Actor actor) {
        final var logger = getLogger(actor.getName());
        currentRootReport.getCurrentReport().success();
        if (logger.isInfoEnabled() && currentRootReport.isFinished()) {
            logger.info(currentRootReport.success().toString());
        }
    }

    @Override
    public <A> void success(Actor actor, A answer) {
        final var logger = getLogger(actor.getName());
        currentRootReport.getCurrentReport().success(format("→ %s", answer));
        if (logger.isInfoEnabled() && currentRootReport.isFinished()) {
            logger.info(currentRootReport.toString());
        }
    }

    @Override
    public void failure(Actor actor, Exception cause) {
        final var logger = getLogger(actor.getName());
        currentRootReport.getCurrentReport().failure(cause.getClass().getSimpleName());
        if (logger.isWarnEnabled() && currentRootReport.isFinished()) {
            logger.warn(currentRootReport.toString());
        }
    }
}
