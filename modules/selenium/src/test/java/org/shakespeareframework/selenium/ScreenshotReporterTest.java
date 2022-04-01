package org.shakespeareframework.selenium;

import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.walk;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.shakespeareframework.Actor;
import org.shakespeareframework.testing.TestQuestionBuilder;
import org.shakespeareframework.testing.TestTaskBuilder;

@Execution(ExecutionMode.SAME_THREAD)
class ScreenshotReporterTest {

  Path reportsPath = Path.of("build", "reports", "shakespeare");
  Actor imogen =
      new Actor("Imogen")
          .can(
              new BrowseTheWeb(
                  new StaticWebDriverSupplier(new NullWebDriver(), BrowserType.CHROME, null)));

  @BeforeEach
  @AfterEach
  void cleanReportsDirectory() throws IOException {
    if (exists(reportsPath)) {
      walk(reportsPath).map(Path::toFile).forEach(File::delete);
      delete(reportsPath);
    }
  }

  @Test
  @DisplayName("starts and successes are not reported by default")
  void test1() {
    var screenshotReporter = new ScreenshotReporter(reportsPath);
    var task = new TestTaskBuilder().string("test task").build();
    var question = new TestQuestionBuilder<String>().string("test question").build();

    screenshotReporter.start(imogen, task);
    screenshotReporter.success(imogen, task);
    screenshotReporter.start(imogen, question);
    screenshotReporter.success(imogen, question, "answer");

    assertThat(reportsPath).doesNotExist();
  }

  @Test
  @DisplayName("successes are reported if configured")
  void test2() {
    var screenshotReporter = new ScreenshotReporter(reportsPath, true);
    var task = new TestTaskBuilder().string("test task").build();
    var question = new TestQuestionBuilder<String>().string("test question").build();

    screenshotReporter.success(imogen, task);
    screenshotReporter.success(imogen, question, "answer");

    assertThat(reportsPath)
        .isDirectoryContaining("glob:build/reports/shakespeare/001-imogen-success-test_task.png")
        .isDirectoryContaining(
            "glob:build/reports/shakespeare/002-imogen-success-test_question.png");
  }

  @Test
  @DisplayName("retries are reported")
  void test3() {
    var screenshotReporter = new ScreenshotReporter(reportsPath);
    var retryableTask = new TestTaskBuilder().string("retryable test task").buildRetryable();
    var retryableQuestion =
        new TestQuestionBuilder<String>().string("retryable test question").buildRetryable();

    screenshotReporter.retry(imogen, retryableTask, new RuntimeException("Fail!"));
    screenshotReporter.retry(imogen, retryableQuestion, "unacceptable answer");
    screenshotReporter.retry(imogen, retryableQuestion, new RuntimeException("Fail!"));

    assertThat(reportsPath)
        .isDirectoryContaining(
            "glob:build/reports/shakespeare/001-imogen-retry-retryable_test_task.png")
        .isDirectoryContaining(
            "glob:build/reports/shakespeare/002-imogen-retry-retryable_test_question.png")
        .isDirectoryContaining(
            "glob:build/reports/shakespeare/003-imogen-retry-retryable_test_question.png");
  }

  @Test
  @DisplayName("failures are reported")
  void test4() {
    var screenshotReporter = new ScreenshotReporter(reportsPath);
    var task = new TestTaskBuilder().string("test task").build();
    var question = new TestQuestionBuilder<String>().string("test question").build();

    screenshotReporter.failure(imogen, task, new RuntimeException("Fail!"));
    screenshotReporter.failure(imogen, question, "unacceptable answer");
    screenshotReporter.failure(imogen, question, new RuntimeException("Fail!"));

    assertThat(reportsPath)
        .isDirectoryContaining("glob:build/reports/shakespeare/001-imogen-failure-test_task.png")
        .isDirectoryContaining(
            "glob:build/reports/shakespeare/002-imogen-failure-test_question.png")
        .isDirectoryContaining(
            "glob:build/reports/shakespeare/003-imogen-failure-test_question.png");
  }
}
