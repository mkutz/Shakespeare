package org.shakespeareframework.selenium;

import static org.openqa.selenium.OutputType.BYTES;
import static org.shakespeareframework.reporting.FileReporter.ReportType.FAILURE;
import static org.shakespeareframework.reporting.FileReporter.ReportType.RETRY;
import static org.shakespeareframework.reporting.FileReporter.ReportType.SUCCESS;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;
import org.openqa.selenium.TakesScreenshot;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.RetryableQuestion;
import org.shakespeareframework.RetryableTask;
import org.shakespeareframework.Task;
import org.shakespeareframework.reporting.FileReporter;

/**
 * Reporter using the {@link Actor}'s {@link BrowseTheWeb#getWebDriver()} as {@link TakesScreenshot}
 * to take a screenshot of the currently displayed site. By default, only retries and failures are
 * reported. If {@link #reportSuccess} is set true, successes are reported as well.
 */
@NullMarked
public class ScreenshotReporter extends FileReporter {

  private final boolean reportSuccess;

  /**
   * @param reportsPath the {@link Path} of the reports directory
   * @param reportSuccess if true, this will report successes as well
   */
  public ScreenshotReporter(Path reportsPath, boolean reportSuccess) {
    super(reportsPath);
    this.reportSuccess = reportSuccess;
  }

  /**
   * Sets {@link #reportSuccess} to false.
   *
   * @param reportsPath the {@link Path} of the reports directory
   */
  public ScreenshotReporter(Path reportsPath) {
    this(reportsPath, false);
  }

  @Override
  public void retry(Actor actor, RetryableTask task, Exception cause) {
    takeScreenshot(actor, RETRY, task);
  }

  @Override
  public void success(Actor actor, Task task) {
    if (reportSuccess) takeScreenshot(actor, SUCCESS, task);
  }

  @Override
  public void failure(Actor actor, Task task, Exception cause) {
    takeScreenshot(actor, FAILURE, task);
  }

  @Override
  public void retry(Actor actor, RetryableQuestion<?> question, Exception cause) {
    takeScreenshot(actor, RETRY, question);
  }

  @Override
  public <A> void retry(Actor actor, RetryableQuestion<A> question, A answer) {
    takeScreenshot(actor, RETRY, question);
  }

  @Override
  public <A> void success(Actor actor, Question<A> question, A answer) {
    if (reportSuccess) takeScreenshot(actor, SUCCESS, question);
  }

  @Override
  public void failure(Actor actor, Question<?> question, Exception cause) {
    takeScreenshot(actor, FAILURE, question);
  }

  @Override
  public <A> void failure(Actor actor, Question<A> question, A answer) {
    takeScreenshot(actor, FAILURE, question);
  }

  private void takeScreenshot(Actor actor, ReportType reportType, Object activity) {
    final var takesScreenshot = (TakesScreenshot) actor.uses(BrowseTheWeb.class).getWebDriver();
    writeReport(actor, reportType, activity, "png", takesScreenshot.getScreenshotAs(BYTES));
  }
}
