package org.shakespeareframework;

import java.time.Duration;

/**
 * {@link Retryable} is something an {@link Actor} may retry every {@link #getInterval() interval}
 * until it succeeds or the {@link #getTimeout() timeout} is reached.
 */
public interface Retryable {

  /** The default timeout */
  Duration DEFAULT_TIMEOUT = Duration.ofSeconds(2);

  /**
   * @return the timeout for this (default: {@link #DEFAULT_TIMEOUT})
   */
  default Duration getTimeout() {
    return DEFAULT_TIMEOUT;
  }

  /**
   * @return the interval to retry this (default {@link #getTimeout()} / 10)
   */
  default Duration getInterval() {
    return DEFAULT_TIMEOUT.dividedBy(10);
  }
}
