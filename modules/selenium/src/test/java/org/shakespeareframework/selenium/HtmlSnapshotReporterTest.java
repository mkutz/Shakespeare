package org.shakespeareframework.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;
import org.shakespeareframework.testing.TestQuestionBuilder;
import org.shakespeareframework.testing.TestTaskBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.*;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlSnapshotReporterTest {

    Path reportsPath = Path.of("build", "reports", "shakespeare");
    Actor tim = new Actor("Tim")
            .can(new BrowseTheWeb(new StaticWebDriverSupplier(new NullWebDriver(), BrowserType.FIREFOX, null)));

    @BeforeEach
    @AfterEach
    void cleanReportsDirectory() throws IOException {
        if (exists(reportsPath)) {
            walk(reportsPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
            delete(reportsPath);
        }
    }

    @Test
    @DisplayName("starts and successes are not reported by default")
    void test1() {
        var htmlSnapshotReporter = new HtmlSnapshotReporter(reportsPath);
        var task = new TestTaskBuilder().string("test task").build();
        var question = new TestQuestionBuilder<String>().string("test question").build();

        htmlSnapshotReporter.start(tim, task);
        htmlSnapshotReporter.success(tim, task);
        htmlSnapshotReporter.start(tim, question);
        htmlSnapshotReporter.success(tim, question, "answer");

        assertThat(reportsPath).doesNotExist();
    }

    @Test
    @DisplayName("successes are reported if configured")
    void test2() {
        var htmlSnapshotReporter = new HtmlSnapshotReporter(reportsPath, true);
        var task = new TestTaskBuilder().string("test task").build();
        var question = new TestQuestionBuilder<String>().string("test question").build();

        htmlSnapshotReporter.success(tim, task);
        htmlSnapshotReporter.success(tim, question, "answer");

        assertThat(reportsPath)
                .isDirectoryContaining("glob:build/reports/shakespeare/001-tim-success-test_task.html")
                .isDirectoryContaining("glob:build/reports/shakespeare/002-tim-success-test_question.html");

    }

    @Test
    @DisplayName("retries are reported")
    void test3() {
        var htmlSnapshotReporter = new HtmlSnapshotReporter(reportsPath);
        var retryableTask = new TestTaskBuilder()
                .string("retryable test task")
                .buildRetryable();
        var retryableQuestion = new TestQuestionBuilder<String>()
                .string("retryable test question")
                .buildRetryable();

        htmlSnapshotReporter.retry(tim, retryableTask, new RuntimeException("Fail!"));
        htmlSnapshotReporter.retry(tim, retryableQuestion, "unacceptable answer");
        htmlSnapshotReporter.retry(tim, retryableQuestion, new RuntimeException("Fail!"));

        assertThat(reportsPath)
                .isDirectoryContaining("glob:build/reports/shakespeare/001-tim-retry-retryable_test_task.html")
                .isDirectoryContaining("glob:build/reports/shakespeare/002-tim-retry-retryable_test_question.html")
                .isDirectoryContaining("glob:build/reports/shakespeare/003-tim-retry-retryable_test_question.html");
    }

    @Test
    @DisplayName("failures are reported")
    void test4() {
        var htmlSnapshotReporter = new HtmlSnapshotReporter(reportsPath);
        var task = new TestTaskBuilder().string("test task").build();
        var question = new TestQuestionBuilder<String>().string("test question").build();

        htmlSnapshotReporter.failure(tim, task, new RuntimeException("Fail!"));
        htmlSnapshotReporter.failure(tim, question, "unacceptable answer");
        htmlSnapshotReporter.failure(tim, question, new RuntimeException("Fail!"));

        assertThat(reportsPath)
                .isDirectoryContaining("glob:build/reports/shakespeare/001-tim-failure-test_task.html")
                .isDirectoryContaining("glob:build/reports/shakespeare/002-tim-failure-test_question.html")
                .isDirectoryContaining("glob:build/reports/shakespeare/003-tim-failure-test_question.html");
    }
}
