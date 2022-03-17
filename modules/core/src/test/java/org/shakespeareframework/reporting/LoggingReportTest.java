package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingReportTest {

    @Test
    @DisplayName("toString started")
    void test1() {
        var report = new LoggingReport("Logan does some task");

        assertThat(report.toString())
                .matches("Logan does some task … (\\d+s)?(<?\\d+ms)");
    }

    @Test
    @DisplayName("toString finished success")
    void test2() {
        var report = new LoggingReport("Logan does some task").success();

        assertThat(report.toString())
                .matches("Logan does some task ✓ (\\d+s)?(<?\\d+ms)");
    }

    @Test
    @DisplayName("toString finished success with supplement")
    void test3() {
        var report = new LoggingReport("Logan checks some question").success("→ answer");

        assertThat(report.toString())
                .matches("Logan checks some question ✓ (\\d+s)?(<?\\d+ms) → answer");
    }

    @Test
    @DisplayName("toString finished failure")
    void test4() {
        var report = new LoggingReport("Logan does some task").failure("cause");

        assertThat(report.toString())
                .matches("Logan does some task ✗ (\\d+s)?(<?\\d+ms) cause");
    }

    @Test
    @DisplayName("toString finished with supplement")
    void test5() {
        var report = new LoggingReport("Logan does some task").failure("cause");

        assertThat(report.toString())
                .matches("Logan does some task ✗ (\\d+s)?(<?\\d+ms) cause");
    }

    @Test
    @DisplayName("toString retry")
    void test6() {
        var report = new LoggingReport("Logan does some task").retry();

        assertThat(report.toString())
                .matches("Logan does some task •… (\\d+s)?(<?\\d+ms)");
    }

    @Test
    @DisplayName("toString sub report")
    void test7() {
        var subReport = new LoggingReport("Logan does some sub task")
                .success();
        var report = new LoggingReport("Logan does some task")
                .addSubReport(subReport)
                .success();

        assertThat(report.toString()).matches("Logan does some task ✓ (\\d+s)?(<?\\d+ms)\n└── " + subReport);
    }

    @Test
    @DisplayName("toString sub sub report")
    void test8() {
        var subSubReport = new LoggingReport("Logan does some sub sub task")
                .success();
        var subReport = new LoggingReport("Logan checks some sub question")
                .addSubReport(subSubReport)
                .success("→ answer");
        var report = new LoggingReport("Logan does some task")
                .addSubReport(subReport)
                .failure("cause");

        assertThat(report.toString()).matches(
                "Logan does some task ✗ (\\d+s)?(<?\\d+ms) cause\n" +
                "└── Logan checks some sub question ✓ (\\d+s)?(<?\\d+ms) → answer\n" +
                "    └── Logan does some sub sub task ✓ (\\d+s)?(<?\\d+ms)");
    }

    @Test
    @DisplayName("toString multiple sub reports")
    void test9() {
        var firstSubReport = new LoggingReport("Logan does some sub task")
                .success();
        var secondSubReport = new LoggingReport("Logan checks some sub question")
                .success("→ answer");
        var report = new LoggingReport("Logan does some task")
                .addSubReport(firstSubReport)
                .addSubReport(secondSubReport)
                .success();

        assertThat(report.toString()).matches(
                "Logan does some task ✓ (\\d+s)?(<?\\d+ms)\n" +
                "├── Logan does some sub task ✓ (\\d+s)?(<?\\d+ms)\n" +
                "└── Logan checks some sub question ✓ (\\d+s)?(<?\\d+ms) → answer");
    }

    @Test
    @DisplayName("getCurrentReport returns the report if there are no sub reports")
    void test10() {
        var report = new LoggingReport("Logan does some task");

        assertThat(report.getCurrentReport()).hasSameHashCodeAs(report);
    }

    @Test
    @DisplayName("getCurrentReport returns latest unfinished sub report")
    void test11() {
        var firstSubReport = new LoggingReport("Logan checks some sub question");
        var secondSubReport = new LoggingReport("Logan checks another sub question");
        var report = new LoggingReport("Logan does some task")
                .addSubReport(firstSubReport)
                .addSubReport(secondSubReport);

        assertThat(report.getCurrentReport()).hasSameHashCodeAs(secondSubReport);
    }

    @Test
    @DisplayName("getCurrentReport does not return finished sub reports")
    void test12() {
        var firstSubReport = new LoggingReport("Logan checks some sub question");
        var secondFinishedSubReport = new LoggingReport("Logan checks some sub question").success();
        var report = new LoggingReport("Logan does some task")
                .addSubReport(firstSubReport)
                .addSubReport(secondFinishedSubReport);

        assertThat(report.getCurrentReport()).hasSameHashCodeAs(firstSubReport);
    }

    @Test
    @DisplayName("getCurrentReport returns the report if all reports are finished")
    void test13() {
        var firstFinishedSubReport = new LoggingReport("Logan checks some sub question")
                .failure("some cause");
        var secondFinishedSubReport = new LoggingReport("Logan checks some sub question")
                .success("answer");
        var finishedReport = new LoggingReport("Logan does some task")
                .addSubReport(firstFinishedSubReport)
                .addSubReport(secondFinishedSubReport)
                .failure();

        assertThat(finishedReport.getCurrentReport()).hasSameHashCodeAs(finishedReport);
    }

    @Test
    @DisplayName("getCurrentReport returns sub sub report")
    void test14() {
        var subSubReport = new LoggingReport("Logan checks some sub sub question");
        var subReport = new LoggingReport("Logan does some sub task")
                .addSubReport(subSubReport);
        var report = new LoggingReport("Logan checks some question")
                .addSubReport(subReport);

        assertThat(report.getCurrentReport()).hasSameHashCodeAs(subSubReport);
    }

    @ParameterizedTest(name = "{0}")
    @EnumSource(FinishedExamples.class)
    void test15(FinishedExamples example) {
        assertThat(example.report.isFinished()).isEqualTo(example.finished);
    }

    enum FinishedExamples {
        SUCCESSFUL(new LoggingReport("Logan does some task").success(), true),
        SUCCESSFUL_WITH_SUCCESSFUL_SUB_REPORT(new LoggingReport("Logan does some task")
                .addSubReport(new LoggingReport("Logan checks some sub question").success())
                .success(), true),
        SUCCESSFUL_WITH_STARTED_SUB_REPORT(new LoggingReport("Logan does some task")
                .addSubReport(new LoggingReport("Logan checks some sub question"))
                .success(), false),
        FAILED(new LoggingReport("Logan does some task").failure(), true),
        STARTED(new LoggingReport("Logan does some task"), false),
        STARTED_WITH_SUCCESSFUL_SUB_REPORT(new LoggingReport("Logan does some task")
                .addSubReport(new LoggingReport("Logan checks some sub question").success()), false);

        private final LoggingReport report;
        private final boolean finished;

        FinishedExamples(LoggingReport report, boolean finished) {
            this.report = report;
            this.finished = finished;
        }

        @Override
        public String toString() {
            return String.format("%s is %s finished",
                    name().toLowerCase().replace('_', ' '), finished ? "" : "not ");
        }
    }
}