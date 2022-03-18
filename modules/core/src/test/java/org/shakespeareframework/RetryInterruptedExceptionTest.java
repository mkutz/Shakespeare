package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RetryInterruptedExceptionTest {

    @Test
    @DisplayName("has meaningful message and correct cause")
    void test1() {
        var cause = new InterruptedException("Something went wrong");
        var retryInterruptedException = new RetryInterruptedException(
                new Actor("Erika"),
                new RetryableTestTaskBuilder().string("some task").perform(actor -> {}),
                cause);

        assertThat(retryInterruptedException)
                .hasMessage("Erika was interrupted while retrying some task")
                .hasCause(cause);
    }
}
