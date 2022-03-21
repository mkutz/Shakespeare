package org.shakespeareframework.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TestQuestionBuilderTest {

    @Test
    @DisplayName("questions have defaults")
    void test1() {
        var question = new TestQuestionBuilder<String>().build();

        assertThat(question.toString()).matches("test question #\\d+");
        assertThat(question.answerAs(new Actor())).isNull();
    }

    @Test
    @DisplayName("question defaults can be overwritten")
    void test2() {
        var question = new TestQuestionBuilder<String>()
                .string("some question")
                .answer(actor -> "answer")
                .build();

        assertThat(question).hasToString("some question");
        assertThat(question.answerAs(new Actor())).isEqualTo("answer");
    }

    @Test
    @DisplayName("retryable questions have defaults")
    void test3() {
        var retryableQuestion = new TestQuestionBuilder<String>().buildRetryable();

        assertThat(retryableQuestion.toString()).matches("test question #\\d+");
        assertThat(retryableQuestion.answerAs(new Actor())).isNull();
        assertThat(retryableQuestion.acceptable("any answer")).isTrue();
        assertThat(retryableQuestion.getTimeout()).isEqualTo(Duration.ofMillis(100));
        assertThat(retryableQuestion.getInterval()).isEqualTo(Duration.ofMillis(10));
        assertThat(retryableQuestion.getIgnoredExceptions()).isEmpty();
    }

    @Test
    @DisplayName("retryable question defaults can be overwritten")
    void test4() {
        var retryableQuestion = new TestQuestionBuilder<String>()
                .string("some retryable question")
                .answer(actor -> "answer")
                .acceptable(givenAnswer -> false)
                .timeout(Duration.ofMillis(50))
                .interval(Duration.ofMillis(5))
                .ignoredExceptions(IOException.class, IllegalArgumentException.class)
                .buildRetryable();

        assertThat(retryableQuestion).hasToString("some retryable question");
        assertThat(retryableQuestion.answerAs(new Actor())).isEqualTo("answer");
        assertThat(retryableQuestion.acceptable("any answer")).isFalse();
        assertThat(retryableQuestion.getTimeout()).isEqualTo(Duration.ofMillis(50));
        assertThat(retryableQuestion.getInterval()).isEqualTo(Duration.ofMillis(5));
        assertThat(retryableQuestion.getIgnoredExceptions())
                .containsExactlyInAnyOrder(IOException.class, IllegalArgumentException.class);
    }
}
