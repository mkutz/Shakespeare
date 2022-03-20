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

public abstract class FileReporter implements Reporter {

    private final Path reportsDirectory;
    private int counter = 0;

    protected FileReporter(Path reportsDirectory) {
        this.reportsDirectory = reportsDirectory;
    }

    /**
     * Writes the given content to a new file in the {@link #reportsDirectory}. The {@link #reportsDirectory} is
     * automatically created if necessary.
     *
     * @param actor             the acting {@link Actor}
     * @param what              a short description what the report is about (e.g. a "failure", "success", "start", "retry")
     * @param activity          the {@link org.shakespeareframework.Task} or {@link org.shakespeareframework.Question}
     *                          this report is about.
     * @param fileNameExtension The file name extension for the report file
     * @param content           the content to write
     * @return the {@link Path} of the new report file
     * @throws RuntimeException if creating the {@link #reportsDirectory} or writing the file fails
     */
    protected Path writeReport(Actor actor, String what, Object activity, String fileNameExtension, byte[] content) {
        try {
            createDirectories(reportsDirectory);
        } catch (IOException e) {
            throw new RuntimeException(format("Failed create reports directory %s", reportsDirectory), e);
        }
        final var reportPath = reportsDirectory.resolve(format("%03d-%s-%s-%s.%s",
                ++counter,
                actor.getName().toLowerCase(Locale.ROOT),
                what.replaceAll("[^\\w]+", "_"),
                activity.toString().replaceAll("[^\\w]+", "_"),
                fileNameExtension));
        try {
            return write(reportPath, content, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(format("Failed create report file %s", reportPath), e);
        }
    }
}
