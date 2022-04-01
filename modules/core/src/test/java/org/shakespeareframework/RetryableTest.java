package org.shakespeareframework;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RetryableTest {

  Retryable retryable = new Retryable() {};

  @Test
  @DisplayName("getTimeout and getInterval return default values")
  void getTimeoutTest1() {
    assertThat(retryable.getTimeout()).isEqualTo(Duration.ofSeconds(2));
    assertThat(retryable.getInterval()).isEqualTo(Duration.ofMillis(200));
  }
}
