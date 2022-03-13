package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionReportTest {

    @Test
    @DisplayName("QuestionReport started")
    void test1() {
        final var report = new QuestionReport("Logan", (actor -> "answer"));

        assertThat(report.asString())
                .matches("Logan checks [^ ]+ (\\d+s)?(<?\\d+ms) …");
    }

    @Test
    @DisplayName("QuestionReport successful")
    void test2() {
        final var answer = UUID.randomUUID().toString();
        final var report = new QuestionReport("Logan", (actor -> answer)).success(answer);

        assertThat(report.asString())
                .matches("Logan checks [^ ]+ (\\d+s)?(<?\\d+ms) ✓\n→ " + answer);
    }

    @Test
    @DisplayName("QuestionReport failure")
    void test3() {
        final var cause = new RuntimeException("Question failed!");
        final var report = new QuestionReport("Logan", (actor -> "answer")).failure(cause);

        assertThat(report.asString())
                .matches("Logan checks [^ ]+ (\\d+s)?(<?\\d+ms) ✗ " + cause.getClass().getSimpleName());
    }
}
