package org.shakespeareframework.reporting;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;
import org.shakespeareframework.testing.TestQuestionBuilder;
import org.shakespeareframework.testing.TestTaskBuilder;

class ReporterTest {

  @Test
  @DisplayName("each task method has a noop default")
  void test1() {
    var defaultReporter = new TaskReporter() {};
    var rachel = new Actor("Rachel");
    var task = new TestTaskBuilder().build();
    var retryableTask = new TestTaskBuilder().buildRetryable();

    assertThatCode(
            () -> {
              defaultReporter.start(rachel, task);
              defaultReporter.retry(rachel, retryableTask, new RuntimeException("Fail!"));
              defaultReporter.success(rachel, task);
              defaultReporter.failure(rachel, task, new RuntimeException("Fail!"));
            })
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("each question method has a noop default")
  void test2() {
    var defaultReporter = new QuestionReporter() {};
    var rachel = new Actor("Rachel");
    var question = new TestQuestionBuilder<String>().build();
    var retryableQuestion = new TestQuestionBuilder<String>().buildRetryable();

    assertThatCode(
            () -> {
              defaultReporter.start(rachel, question);
              defaultReporter.retry(rachel, retryableQuestion, "unacceptable");
              defaultReporter.retry(rachel, retryableQuestion, new RuntimeException("Fail!"));
              defaultReporter.success(rachel, retryableQuestion, "answer");
              defaultReporter.failure(rachel, retryableQuestion, "unacceptable");
              defaultReporter.failure(rachel, retryableQuestion, new RuntimeException("Fail!"));
            })
        .doesNotThrowAnyException();
  }
}
