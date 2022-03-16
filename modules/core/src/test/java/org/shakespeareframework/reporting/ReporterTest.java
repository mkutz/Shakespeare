package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;
import org.shakespeareframework.TestQuestionBuilder;
import org.shakespeareframework.TestTaskBuilder;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReporterTest {

    @Test
    @DisplayName("each method has a noop default")
    void test1() {
        var defaultReporter = new Reporter() {};
        var rachel = new Actor("Rachel");

        assertThatCode(() -> {
                    defaultReporter.start(rachel, new TestTaskBuilder().perform(actor -> {}));
                    defaultReporter.success(rachel);
                    defaultReporter.start(rachel, new TestQuestionBuilder<String>().answer(actor -> "answer"));
                    defaultReporter.retry(rachel, "unacceptable");
                    defaultReporter.retry(rachel, new RuntimeException("Fail!"));
                    defaultReporter.success(rachel, "answer");
                    defaultReporter.failure(rachel, new RuntimeException("Fail!"));
                }
        ).doesNotThrowAnyException();
    }
}
