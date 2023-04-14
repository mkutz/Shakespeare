package org.shakespeareframework;

import java.time.Duration;

/**
 * {@link Retryable} is something an {@link Actor} may retry every {@link #getInterval() interval}
 * until it succeeds or the {@link #getTimeout() timeout} is reached.
 */
public interface Retryable {

    /**
     * The default timeout
     */
    Duration DEFAULT_TIMEOUT = Duration.ofSeconds(2);
    Integer retryLimit = null;
    Integer retryNumber = 0;

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

    /**
     * @return the maximum amount of allowed retries (default {@link #retryLimit})
     */
    default Integer getMaxRetries() {
        return retryLimit;
    }

    /**
     * @return the number of the current retry (default {@link #retryNumber})
     */
    default Integer getRetryNumber() {
        return retryNumber;
    }

    /**
     * @return false if a retry limit is set and the current retry number surpasses given limit (default {@link true})
     * function call increases retry number by one
     */
    default boolean surpassesRetryLimit() {
        if (retryLimit == null) return false;
        if (retryNumber > retryLimit) return true;
        retryNumber++;
        return false;
    }
}
