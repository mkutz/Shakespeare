package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.shakespeareframework.*;

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
                .perform(actor -> {});
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
                .perform(actor -> {});

        reporter.start(logan, task);
        reporter.success(logan);

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
                .perform(actor -> {});

        reporter.start(logan, task);
        reporter.failure(logan, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan does some task ✗ (\\d+s)?(<?\\d+ms) RuntimeException");
    }

    @Test
    @DisplayName("retried and finished task is logged as warning")
    void test4() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var task = new TestTaskBuilder()
                .string("some task")
                .perform(actor -> {});

        reporter.start(logan, task);
        reporter.retry(logan, new RuntimeException("Retry failed"));
        reporter.failure(logan, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan does some task •✗ (\\d+s)?(<?\\d+ms) RuntimeException");
    }

    @Test
    @DisplayName("unfinished question is not logged")
    void test5() {
        var reporter = new Slf4jReporter();
        var question = new TestQuestionBuilder<String>()
                .string("some question")
                .answer(actor -> "answer");
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
                .answer(actor -> "answer");

        reporter.start(logan, question);
        reporter.success(logan, "answer");

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
                .answer(actor -> "answer");

        reporter.start(logan, question);
        reporter.failure(logan, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan checks some question ✗ (\\d+s)?(<?\\d+ms) RuntimeException");
    }

    @Test
    @DisplayName("retried and unsuccessfully finished question is logged as warning")
    void test8() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var question = new TestQuestionBuilder<String>()
                .string("some question")
                .answer(actor -> "answer");

        reporter.start(logan, question);
        reporter.retry(logan, "unaccepted answer");
        reporter.failure(logan, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern("Logan checks some question •✗ (\\d+s)?(<?\\d+ms) RuntimeException");
    }

    @Test
    @DisplayName("successful task with sub question is logged as info")
    void test9() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var rootTask = new TestTaskBuilder()
                .string("some root task")
                .perform(actor -> {});
        var subQuestion = new TestQuestionBuilder<String>()
                .string("some sub question")
                .answer(actor -> "answer");

        reporter.start(logan, rootTask);
        reporter.start(logan, subQuestion);
        reporter.success(logan, "answer");
        reporter.success(logan);

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
                .answer(actor -> "answer");
        var subTask = new TestTaskBuilder()
                .string("some sub task")
                .perform(actor -> {});

        reporter.start(logan, rootQuestion);
        reporter.start(logan, subTask);
        reporter.success(logan, "answer");
        reporter.failure(logan, new RuntimeException("Fail!"));

        assertThat(output.getOut())
                .contains("WARN")
                .containsPattern(
                        "Logan checks some root question ✗ (\\d+s)?(<?\\d+ms) RuntimeException\n" +
                        "└── Logan does some sub task ✓ (\\d+s)?(<?\\d+ms)");
    }
}