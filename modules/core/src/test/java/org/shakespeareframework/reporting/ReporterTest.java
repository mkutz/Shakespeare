package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.*;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReporterTest {

    @Test
    @DisplayName("each method has a noop default")
    void test1() {
        var defaultReporter = new Reporter() {};
        var rachel = new Actor("Rachel");
        var task = new TestTaskBuilder().perform(actor -> {});
        var question = new TestQuestionBuilder<String>().answer(actor -> "answer");
        var retryableQuestion = new RetryableTestQuestionBuilder<String>().answer(actor -> "answer");

        assertThatCode(() -> {
                    defaultReporter.start(rachel, task);
                    defaultReporter.success(rachel, task);
                    defaultReporter.start(rachel, question);
                    defaultReporter.retry(rachel, retryableQuestion, "unacceptable");
                    defaultReporter.retry(rachel, retryableQuestion, new RuntimeException("Fail!"));
                    defaultReporter.success(rachel, retryableQuestion, "answer");
                    defaultReporter.failure(rachel, task, new RuntimeException("Fail!"));
                }
        ).doesNotThrowAnyException();
    }
}
