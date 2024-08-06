package org.shakespeareframework.reporting;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.RetryableQuestion;
import org.shakespeareframework.RetryableTask;
import org.shakespeareframework.Task;

/** {@link LoggingReporter} that logs to SLF4J. */
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

  private void retry() {
    currentRootReport.getCurrentReport().retry();
  }

  @Override
  public void retry(Actor actor, RetryableTask task, Exception cause) {
    retry();
  }

  @Override
  public <A> void retry(Actor actor, RetryableQuestion<A> question, A answer) {
    retry();
  }

  @Override
  public void retry(Actor actor, RetryableQuestion<?> question, Exception cause) {
    retry();
  }

  private void success(Actor actor) {
    final var logger = getLogger(actor.getName());
    currentRootReport.getCurrentReport().success();
    if (logger.isInfoEnabled() && currentRootReport.isFinished()) {
      logger.info(currentRootReport.success().toString());
      currentRootReport = null;
    }
  }

  @Override
  public void success(Actor actor, Task task) {
    success(actor);
  }

  @Override
  public <A> void success(Actor actor, Question<A> question, A answer) {
    final var logger = getLogger(actor.getName());
    currentRootReport.getCurrentReport().success(format("→ %s", answer));
    if (logger.isInfoEnabled() && currentRootReport.isFinished()) {
      logger.info(currentRootReport.toString());
      currentRootReport = null;
    }
  }

  private void failure(Actor actor, Exception cause) {
    final var logger = getLogger(actor.getName());
    currentRootReport.getCurrentReport().failure(cause.getClass().getSimpleName());
    if (logger.isWarnEnabled() && currentRootReport.isFinished()) {
      logger.warn(currentRootReport.toString());
      currentRootReport = null;
    }
  }

  @Override
  public void failure(Actor actor, Task task, Exception cause) {
    failure(actor, cause);
  }

  @Override
  public void failure(Actor actor, Question<?> question, Exception cause) {
    failure(actor, cause);
  }

  @Override
  public <A> void failure(Actor actor, Question<A> question, A answer) {
    final var logger = getLogger(actor.getName());
    currentRootReport.getCurrentReport().failure(format("→ %s", answer));
    if (logger.isWarnEnabled() && currentRootReport.isFinished()) {
      logger.warn(currentRootReport.toString());
      currentRootReport = null;
    }
  }
}
