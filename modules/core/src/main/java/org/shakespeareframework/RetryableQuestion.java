package org.shakespeareframework;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link Question} that will be retried until it yields an answer deemed {@link #acceptable} or the
 * {@link Retryable#getTimeout() timeout} is reached. {@link Exception}s thrown will be ignored if they are contained in
 * {@link #getIgnoredExceptions() ignoredExceptions}.
 */
public interface RetryableQuestion<A> extends Question<A>, Retryable {

    /**
     * @return the {@link Set} of {@link Exception} classes that will be ignored when thrown in a retry
     */
    default Set<Class<? extends Exception>> getIgnoredExceptions() {
        return Set.of();
    }

    /**
     * <p>This determines if a given answer is acceptable. In that case the retrying will be stopped and the answer is
     * returned.</p>
     *
     * <p>By default this will accept</p>
     *
     * <ul>
     *     <li>a {@link Optional#isPresent()},</li>
     *     <li>a !{@link Collection#isEmpty()},</li>
     *     <li>a !{@link Map#isEmpty()},</li>
     *     <li>an array with length &gt; 0, and</li>
     *     <li>a {@link Boolean#TRUE}.</li>
     * </ul>
     *
     * @param answer an answer
     * @return {@code true} if the given answer is acceptable
     */
    default boolean acceptable(A answer) {
        if (answer instanceof Optional<?>) {
            return ((Optional<?>) answer).isPresent();
        } else if (answer instanceof Collection<?>) {
            return !((Collection<?>) answer).isEmpty();
        } else if (answer instanceof Map<?, ?>) {
            return !((Map<?, ?>) answer).isEmpty();
        } else if (answer instanceof Object[]) {
            return ((Object[]) answer).length > 0;
        } else if (answer instanceof Boolean) {
            return (Boolean) answer;
        }
        return answer != null;
    }
}
