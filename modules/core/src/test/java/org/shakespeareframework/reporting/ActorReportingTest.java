package org.shakespeareframework.reporting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.Task;
import org.shakespeareframework.TimeoutException;
import org.shakespeareframework.testing.TestQuestionBuilder;
import org.shakespeareframework.testing.TestTaskBuilder;

class ActorReportingTest {

  TestReporter testReporter = new TestReporter();

  Actor actor = new Actor().informs(testReporter);

  @Test
  @DisplayName("does is reported")
  void doesTest1() {
    Task task = (actor) -> {};

    actor.does(task);

    assertThat(testReporter.getReports()).hasSize(1);
    TestReporter.Report<?> report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(task);
    assertThat(report.isSuccess()).isTrue();
  }

  @Test
  @DisplayName("does is reported on failure")
  void doesTest2() {
    var cause = new RuntimeException("Failed!");
    Task failingTask =
        (actor) -> {
          throw cause;
        };

    assertThatThrownBy(() -> actor.does(failingTask)).isEqualTo(cause);

    assertThat(testReporter.getReports()).hasSize(1);
    TestReporter.Report<?> report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(failingTask);
    assertThat(report.isSuccess()).isFalse();
    assertThat(report.getCause()).isEqualTo(cause);
  }

  @Test
  @DisplayName("does is reported on retry")
  void doesTest3() {
    var called = new AtomicInteger(0);
    var cause = new RuntimeException("Failed!");
    var retryableTask =
        new TestTaskBuilder()
            .perform(
                actor -> {
                  if (called.incrementAndGet() < 2) throw cause;
                })
            .buildRetryable();

    actor.does(retryableTask);

    assertThat(testReporter.getReports()).hasSize(1);
    TestReporter.Report<?> report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(retryableTask);
    assertThat(report.isSuccess()).isTrue();
    assertThat(report.getRetries()).isEqualTo(1);
  }

  @Test
  @DisplayName("does is reported on retry timeout")
  void doesTest4() {
    var cause = new RuntimeException("Failed!");
    var timeoutRetryableTask =
        new TestTaskBuilder()
            .perform(
                actor -> {
                  throw cause;
                })
            .buildRetryable();

    assertThatThrownBy(() -> actor.does(timeoutRetryableTask))
        .isInstanceOf(TimeoutException.class)
        .hasCause(cause);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(timeoutRetryableTask);
    assertThat(report.isSuccess()).isFalse();
    assertThat(report.getCause()).isInstanceOf(TimeoutException.class).hasCause(cause);
    assertThat(report.getRetries()).isCloseTo(10, within(1));
  }

  @Test
  @DisplayName("does is reported on retry failure")
  void doesTest5() {
    var cause = new RuntimeException("Failed!");
    var failingRetryableTask =
        new TestTaskBuilder()
            .acknowledgedExceptions(cause.getClass())
            .perform(
                actor -> {
                  throw cause;
                })
            .buildRetryable();

    assertThatThrownBy(() -> actor.does(failingRetryableTask)).isEqualTo(cause);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(failingRetryableTask);
    assertThat(report.isSuccess()).isFalse();
    assertThat(report.getCause()).isEqualTo(cause);
    assertThat(report.getRetries()).isZero();
  }

  @Test
  @DisplayName("checks is reported")
  void checksTest1() {
    var answer = "answer";
    Question<String> question = (actor) -> answer;

    actor.checks(question);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(question);
    assertThat(report.isSuccess()).isTrue();
    assertThat(report.getAnswer()).isEqualTo(answer);
  }

  @Test
  @DisplayName("checks is reported on failure")
  void checksTest2() {
    var cause = new RuntimeException("Failed!");
    Question<String> failingQuestion =
        (actor) -> {
          throw cause;
        };

    assertThatThrownBy(() -> actor.checks(failingQuestion)).isEqualTo(cause);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(failingQuestion);
    assertThat(report.isSuccess()).isFalse();
    assertThat(report.getCause()).isEqualTo(cause);
  }

  @Test
  @DisplayName("checks is reported on retry")
  void checksTest3() {
    var called = new AtomicInteger(0);
    var answer = "answer";
    var retryableQuestion =
        new TestQuestionBuilder<String>()
            .acceptable(string -> string.equals(answer))
            .answer(
                actor -> {
                  if (called.incrementAndGet() < 2) return "unacceptable";
                  return answer;
                })
            .buildRetryable();

    actor.checks(retryableQuestion);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(retryableQuestion);
    assertThat(report.isSuccess()).isTrue();
    assertThat(report.getRetries()).isEqualTo(1);
  }

  @Test
  @DisplayName("checks is reported on retry timeout due to answer")
  void checksTest4() {
    var timeoutRetryableQuestion =
        new TestQuestionBuilder<String>()
            .acceptable(answer -> false)
            .answer(actor -> "unacceptable")
            .buildRetryable();

    assertThatThrownBy(() -> actor.checks(timeoutRetryableQuestion))
        .isInstanceOf(TimeoutException.class);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(timeoutRetryableQuestion);
    assertThat(report.isSuccess()).isFalse();
    assertThat(report.getCause()).isInstanceOf(TimeoutException.class);
    assertThat(report.getRetries()).isCloseTo(10, within(1));
  }

  @Test
  @DisplayName("checks is reported on retry timeout due to exception")
  void checksTest5() {
    var cause = new RuntimeException("Failed!");
    var failingRetryableQuestion =
        new TestQuestionBuilder<String>()
            .ignoredExceptions(cause.getClass())
            .answer(
                actor -> {
                  throw cause;
                })
            .buildRetryable();

    assertThatThrownBy(() -> actor.checks(failingRetryableQuestion))
        .isInstanceOf(TimeoutException.class)
        .hasCause(cause);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(failingRetryableQuestion);
    assertThat(report.isSuccess()).isFalse();
    assertThat(report.getCause()).isInstanceOf(TimeoutException.class).hasCause(cause);
    assertThat(report.getRetries()).isCloseTo(10, within(1));
  }

  @Test
  @DisplayName("checks is reported on retry failure")
  void doesTest6() {
    var cause = new RuntimeException("Failed!");
    var failingRetryableQuestion =
        new TestQuestionBuilder<String>()
            .answer(
                actor -> {
                  throw cause;
                })
            .buildRetryable();

    assertThatThrownBy(() -> actor.checks(failingRetryableQuestion)).isEqualTo(cause);

    assertThat(testReporter.getReports()).hasSize(1);
    var report = testReporter.getReports().pop();
    assertThat(report.getSubject()).isEqualTo(failingRetryableQuestion);
    assertThat(report.isSuccess()).isFalse();
    assertThat(report.getCause()).isEqualTo(cause);
    assertThat(report.getRetries()).isZero();
  }
}
