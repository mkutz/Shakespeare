package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskReportTest {

    @Test
    @DisplayName("TaskReport started")
    void test1() {
        final var report = new TaskReport("Logan", (actor -> {}));

        assertThat(report.asString())
                .matches("Logan does [^ ]+ (\\d+s)?(<?\\d+ms) …");
    }

    @Test
    @DisplayName("TaskReport successful")
    void test2() {
        final var report = new TaskReport("Logan", (actor -> {})).success();

        assertThat(report.asString())
                .matches("Logan does [^ ]+ (\\d+s)?(<?\\d+ms) ✓");
    }

    @Test
    @DisplayName("TaskReport failed")
    void test3() {
        final var cause = new RuntimeException("Task failed!");
        final var report = new TaskReport("Logan", (actor -> {})).failure(cause);

        assertThat(report.asString())
                .matches("Logan does [^ ]+ (\\d+s)?(<?\\d+ms) ✗ " + cause.getClass().getSimpleName());
    }
}
