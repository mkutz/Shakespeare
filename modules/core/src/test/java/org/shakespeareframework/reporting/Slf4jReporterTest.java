package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.shakespeareframework.*;
import org.shakespeareframework.testing.TestQuestionBuilder;
import org.shakespeareframework.testing.TestTaskBuilder;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class Slf4jReporterTest {

    @RegisterExtension
    OutputTestExtension output = new OutputTestExtension();

    @Test
    @DisplayName("unfinished task is not logged")
    void test1() {
        var reporter = new Slf4jReporter();
        var task = new TestTaskBuilder()
                .string("some task")
                .build();
        reporter.start(new Actor("Logan"), task);

        assertThat(output.getOut()).isEmpty();
    }

    @Test
    @DisplayName("successfully finished task is logged as info")
    void test2() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var task = new TestTaskBuilder()
                .string("some task")
                .build();

        reporter.start(logan, task);
        reporter.success(logan, task);

        assertThat(output.getOut())
                .contains("INFO")
                .containsPattern("Logan does some task ✓ (\\d+s)?(<?\\d+ms)");
    }

    @Test
    @DisplayName("unsuccessfully finished task is logged as warning")
    void test3() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var task = new TestTaskBuilder()
                .string("some task")
                .build();

        reporter.start(logan, task);
        reporter.failure(logan, task, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan does some task ✗ (\\d+s)?(<?\\d+ms) RuntimeException");
    }

    @Test
    @DisplayName("retried and finished task is logged as warning")
    void test4() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var retryableTask = new TestTaskBuilder()
                .string("some retryable task")
                .buildRetryable();

        reporter.start(logan, retryableTask);
        reporter.retry(logan, retryableTask, new RuntimeException("Retry failed"));
        reporter.failure(logan, retryableTask, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan does some retryable task •✗ (\\d+s)?(<?\\d+ms) RuntimeException");
    }

    @Test
    @DisplayName("unfinished question is not logged")
    void test5() {
        var reporter = new Slf4jReporter();
        var question = new TestQuestionBuilder<String>()
                .answer(actor -> "answer")
                .build();
        reporter.start(new Actor("Logan"), question);

        assertThat(output.getOut()).isEmpty();
    }

    @Test
    @DisplayName("successfully finished question is logged as info")
    void test6() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var question = new TestQuestionBuilder<String>()
                .string("some question")
                .answer(actor -> "answer")
                .build();

        reporter.start(logan, question);
        reporter.success(logan, question, "answer");

        assertThat(output.getOut())
                .contains("INFO")
                .containsPattern("Logan checks some question ✓ (\\d+s)?(<?\\d+ms) → answer");
    }

    @Test
    @DisplayName("unsuccessfully finished question is logged as warning")
    void test7() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var question = new TestQuestionBuilder<String>()
                .string("some question")
                .answer(actor -> "answer")
                .build();

        reporter.start(logan, question);
        reporter.failure(logan, question, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan checks some question ✗ (\\d+s)?(<?\\d+ms) RuntimeException");
    }

    @Test
    @DisplayName("retried and unsuccessfully finished question is logged as warning")
    void test8() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var retryableQuestion = new TestQuestionBuilder<String>()
                .string("some retryable question")
                .answer(actor -> "answer")
                .buildRetryable();

        reporter.start(logan, retryableQuestion);
        reporter.retry(logan, retryableQuestion, new RuntimeException("Intermediate failure!"));
        reporter.retry(logan, retryableQuestion, "intermediate answer");
        reporter.failure(logan, retryableQuestion, "unaccepted answer");

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan checks some retryable question ••✗ (\\d+s)?(<?\\d+ms) → unaccepted answer");
    }

    @Test
    @DisplayName("successful task with sub question is logged as info")
    void test9() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var rootTask = new TestTaskBuilder()
                .string("some root task")
                .build();
        var subQuestion = new TestQuestionBuilder<String>()
                .string("some sub question")
                .build();

        reporter.start(logan, rootTask);
        reporter.start(logan, subQuestion);
        reporter.success(logan, subQuestion, "answer");
        reporter.success(logan, rootTask);

        assertThat(output.getOut())
                .contains("INFO")
                .containsPattern(
                        "Logan does some root task ✓ (\\d+s)?(<?\\d+ms)\n" +
                        "└── Logan checks some sub question ✓ (\\d+s)?(<?\\d+ms) → answer");
    }

    @Test
    @DisplayName("unsuccessful question with sub task is logged as warning")
    void test10() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var rootQuestion = new TestQuestionBuilder<String>()
                .string("some root question")
                .answer(actor -> "answer")
                .build();
        var subTask = new TestTaskBuilder()
                .string("some sub task")
                .build();

        reporter.start(logan, rootQuestion);
        reporter.start(logan, subTask);
        reporter.success(logan, subTask);
        reporter.failure(logan, rootQuestion, new RuntimeException("Fail!"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern(
                        "Logan checks some root question ✗ (\\d+s)?(<?\\d+ms) RuntimeException\n" +
                        "└── Logan does some sub task ✓ (\\d+s)?(<?\\d+ms)");
    }
}
