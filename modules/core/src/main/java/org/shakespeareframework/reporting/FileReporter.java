package org.shakespeareframework.reporting;

import org.shakespeareframework.Actor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

import static java.lang.String.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.write;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * {@link Reporter} providing a method to write report files. Files will be placed under the {@link #reportsPath}
 * and named {@code counter}-{@code actor}-{@code reportType}-{@code task|question}.{@code fileNameExtension}. E.g.:
 * {@code 003-regina-retry-is_logged_in.png}.
 */
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
     * @param actor             the acting {@link Actor}
     * @param reportType        the {@link ReportType}
     * @param activity          the {@link org.shakespeareframework.Task} or {@link org.shakespeareframework.Question}
     *                          this report is about.
     * @param fileNameExtension The file name extension for the report file
     * @param content           the content to write
     * @throws RuntimeException if creating the {@link #reportsPath} or writing the file fails
     */
    protected void writeReport(Actor actor, ReportType reportType, Object activity, String fileNameExtension,byte[] content) {
        try {
            createDirectories(reportsPath);
        } catch (IOException e) {
            throw new RuntimeException(format("Failed create reports directory %s", reportsPath), e);
        }
        final var reportPath = reportsPath.resolve(format("%03d-%s-%s-%s.%s",
                ++counter,
                actor.getName().toLowerCase(Locale.ROOT),
                reportType,
                activity.toString().replaceAll("[^\\w]+", "_"),
                fileNameExtension));
        try {
            write(reportPath, content, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(format("Failed create report file %s", reportPath), e);
        }
    }

    /**
     * Possible types of reports.
     */
    public enum ReportType {
        START, RETRY, SUCCESS, FAILURE;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
