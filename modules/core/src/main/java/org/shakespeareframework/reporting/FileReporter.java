package org.shakespeareframework.reporting;

import static java.lang.String.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.write;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.shakespeareframework.Actor;

/**
 * {@link Reporter} providing a method to write report files. Files will be placed under the {@link
 * #reportsPath} and named {@code counter}-{@code actor}-{@code reportType}-{@code
 * task|question}.{@code fileNameExtension}. E.g.: {@code 003-regina-retry-is_logged_in.png}.
 */
@NullMarked
public abstract class FileReporter implements Reporter {

  private final Path reportsPath;
  private int counter = 0;

  /**
   * @param reportsPath path to the reports directory
   */
  protected FileReporter(Path reportsPath) {
    this.reportsPath = reportsPath;
  }

  /**
   * Writes the given content to a new file in the {@link #reportsPath}. The {@link #reportsPath} is
   * automatically created if necessary.
   *
   * <p>The filename will be structured as follows:
   *
   * <p><code>
   * [increased {@link #counter} with leading zeros]-[{@link Actor#getName() actor.name}]-
   * [{@link ReportType reportType}]-[activity].[fileNameExtension]
   * </code>
   *
   * <p>E.g. <code>001-fiona-start-some_question.txt</code>
   *
   * @param actor the acting {@link Actor}
   * @param reportType the {@link ReportType}
   * @param activity the {@link org.shakespeareframework.Task} or {@link
   *     org.shakespeareframework.Question} this report is about.
   * @param fileNameExtension The file name extension for the report file
   * @param content the content to write
   * @throws RuntimeException if creating the {@link #reportsPath} or writing the file fails
   */
  protected void writeReport(
      Actor actor,
      ReportType reportType,
      Object activity,
      String fileNameExtension,
      byte[] content) {
    try {
      createDirectories(reportsPath);
    } catch (IOException e) {
      throw new WriteReportFileException(
          format("Failed create reports directory %s", reportsPath), e);
    }
    final var reportPath =
        reportsPath.resolve(
            format(
                "%03d-%.20s-%.7s-%.100s.%.10s",
                ++counter,
                actor.getName().toLowerCase(Locale.ROOT),
                reportType,
                activity.toString().replaceAll("\\W+", "_"),
                fileNameExtension));
    try {
      write(reportPath, content, CREATE, TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new WriteReportFileException(format("Failed create report file %s", reportPath), e);
    }
  }

  /** Possible types of reports. */
  public enum ReportType {
    /** something was started */
    START,
    /** something was retried */
    RETRY,
    /** something was finished successfully */
    SUCCESS,
    /** something was finished unsuccessfully */
    FAILURE;

    @Override
    public String toString() {
      return name().toLowerCase(Locale.ROOT);
    }
  }
}
