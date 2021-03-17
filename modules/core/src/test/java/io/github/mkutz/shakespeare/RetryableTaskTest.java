package io.github.mkutz.shakespeare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RetryableTaskTest {

    RetryableTask retryableTask = (actor) -> {};

    @Test
    @DisplayName("getAcknowledgedExceptions is empty by default")
    void getAcknowledgedExceptionsTest1() {
        assertThat(retryableTask.getAcknowledgedExceptions()).isEqualTo(Set.of());
    }
}