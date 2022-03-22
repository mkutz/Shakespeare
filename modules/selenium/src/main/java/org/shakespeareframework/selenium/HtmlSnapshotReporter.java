package org.shakespeareframework.selenium;

import org.shakespeareframework.*;
import org.shakespeareframework.reporting.FileReporter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.shakespeareframework.reporting.FileReporter.ReportType.*;

/**
 * Reporter using the {@link Actor}'s {@link BrowseTheWeb#getWebDriver()} to take an HTML snapshot of the currently
 * displayed site. By default, only retries and failures are reported. If {@link #reportSuccess} is set true,
 * successes are reported as well.
 */
public class HtmlSnapshotReporter extends FileReporter {

    private final boolean reportSuccess;

    /**
     * @param reportsPath   the {@link Path} of the reports directory
     * @param reportSuccess if true, this will report successes as well
     */
    public HtmlSnapshotReporter(Path reportsPath, boolean reportSuccess) {
        super(reportsPath);
        this.reportSuccess = reportSuccess;
    }

    /**
     * Sets {@link #reportSuccess} to false.
     *
     * @param reportsPath the {@link Path} of the reports directory
     */
    public HtmlSnapshotReporter(Path reportsPath) {
        this(reportsPath, false);
    }

    @Override
    public void retry(Actor actor, RetryableTask task, Exception cause) {
        takeHtmlSnapshot(actor, RETRY, task);
    }

    @Override
    public void success(Actor actor, Task task) {
        if (reportSuccess) takeHtmlSnapshot(actor, SUCCESS, task);
    }

    @Override
    public void failure(Actor actor, Task task, Exception cause) {
        takeHtmlSnapshot(actor, FAILURE, task);
    }

    @Override
    public void retry(Actor actor, RetryableQuestion<?> question, Exception cause) {
        takeHtmlSnapshot(actor, RETRY, question);
    }

    @Override
    public <A> void retry(Actor actor, RetryableQuestion<A> question, A answer) {
        takeHtmlSnapshot(actor, RETRY, question);
    }

    @Override
    public <A> void success(Actor actor, Question<A> question, A answer) {
        if (reportSuccess) takeHtmlSnapshot(actor, SUCCESS, question);
    }

    @Override
    public void failure(Actor actor, Question<?> question, Exception cause) {
        takeHtmlSnapshot(actor, FAILURE, question);
    }

    @Override
    public <A> void failure(Actor actor, Question<A> question, A answer) {
        takeHtmlSnapshot(actor, FAILURE, question);
    }

    private void takeHtmlSnapshot(Actor actor, ReportType reportType, Object activity) {
        final var webDriver = actor.uses(BrowseTheWeb.class).getWebDriver();
        writeReport(actor, reportType, activity, "html",
                webDriver.getPageSource().getBytes(StandardCharsets.UTF_8));
    }
}
