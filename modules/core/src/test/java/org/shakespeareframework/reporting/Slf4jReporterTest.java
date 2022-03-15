package org.shakespeareframework.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.shakespeareframework.Actor;
import org.shakespeareframework.OutputTestExtension;
import org.shakespeareframework.Task;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class Slf4jReporterTest {

    @RegisterExtension
    OutputTestExtension output = new OutputTestExtension();

    @Test
    @DisplayName("unfinished reports are not logged")
    void test1() {
        var reporter = new Slf4jReporter();
        reporter.start(new Actor("Logan"), (actor) -> {});

        assertThat(output.getOut()).isEmpty();
    }

    @Test
    @DisplayName("successfully finished task is logged as info")
    void test2() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        var task = new Task() {
            @Override
            public void performAs(Actor actor) {}

            @Override
            public String toString() {
                return "some task";
            }
        };

        reporter.start(logan, task);
        reporter.success(logan);

        assertThat(output.getOut())
                .contains("INFO")
                .containsPattern(logan.getName() + " does " + task + " ✓ (\\d+s)?(<?\\d+ms)");
    }

    @Test
    @DisplayName("unsuccessfully finished task is logged as warning")
    void test3() {
        var reporter = new Slf4jReporter();
        var logan = new Actor("Logan");
        Task task = (actor) -> {};

        reporter.start(logan, task);
        reporter.failure(logan, new RuntimeException("Fail"));

        assertThat(output.getOut())
                .contains("WARN")
                .contains(format("%s does %s", logan.getName(), task));
    }
}
