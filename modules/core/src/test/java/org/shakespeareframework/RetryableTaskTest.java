package org.shakespeareframework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RetryableTaskTest {

  RetryableTask retryableTask = (actor) -> {};

  @Test
  @DisplayName("getAcknowledgedExceptions is empty by default")
  void getAcknowledgedExceptionsTest1() {
    assertThat(retryableTask.getAcknowledgedExceptions()).isEqualTo(Set.of());
  }
}
