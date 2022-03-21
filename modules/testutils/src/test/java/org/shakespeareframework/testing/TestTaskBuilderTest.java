package org.shakespeareframework.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

class TestTaskBuilderTest {

    @Test
    @DisplayName("tasks have defaults")
    void test1() {
        var task = new TestTaskBuilder().build();

        assertThat(task.toString()).matches("test task #\\d+");
        assertThatNoException().isThrownBy(() -> task.performAs(new Actor()));
    }

    @Test
    @DisplayName("task defaults can be overwritten")
    void test2() {
        var bettie = new Actor("Bettie");
        var task = new TestTaskBuilder()
                .string("some task")
                .perform(actor -> { throw new RuntimeException("Fail!"); })
                .build();

        assertThat(task).hasToString("some task");
        assertThatThrownBy(() -> task.performAs(bettie))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Fail!");
    }

    @Test
    @DisplayName("retryable tasks have defaults")
    void test3() {
        var retryableTask = new TestTaskBuilder().buildRetryable();

        assertThat(retryableTask.toString()).matches("test task #\\d+");
        assertThatNoException().isThrownBy(() -> retryableTask.performAs(new Actor()));
        assertThat(retryableTask.getTimeout()).isEqualTo(Duration.ofMillis(100));
        assertThat(retryableTask.getInterval()).isEqualTo(Duration.ofMillis(10));
        assertThat(retryableTask.getAcknowledgedExceptions()).isEmpty();
    }

    @Test
    @DisplayName("retryable task defaults can be overwritten")
    void test4() {
        var bettie = new Actor("Bettie");
        var retryableTask = new TestTaskBuilder()
                .string("some retryable task")
                .perform(actor -> { throw new RuntimeException("Fail!"); })
                .timeout(Duration.ofMillis(50))
                .interval(Duration.ofMillis(5))
                .acknowledgedExceptions(IOException.class, IllegalArgumentException.class)
                .buildRetryable();

        assertThat(retryableTask).hasToString("some retryable task");
        assertThatThrownBy(() -> retryableTask.performAs(bettie))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Fail!");
        assertThat(retryableTask.getTimeout()).isEqualTo(Duration.ofMillis(50));
        assertThat(retryableTask.getInterval()).isEqualTo(Duration.ofMillis(5));
        assertThat(retryableTask.getAcknowledgedExceptions())
                .containsExactlyInAnyOrder(IOException.class, IllegalArgumentException.class);
    }
}
