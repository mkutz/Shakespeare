package org.shakespeareframework;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link Question} that will be retried until it yields an answer deemed {@link #acceptable} or
 * the {@link Retryable#getTimeout() timeout} is reached. {@link Exception}s thrown will be ignored
 * if they match the {@link #isIgnoredException(Exception) isIgnoredException predicate}.
 */
public interface RetryableQuestion<A> extends Question<A>, Retryable {

  /**
   * @return the {@link Set} of {@link Exception} classes that will be ignored when thrown in a
   *     retry
   */
  default Set<Class<? extends Exception>> getIgnoredExceptions() {
    return Set.of();
  }

  /**
   * @param exception the {@link Exception} that made the retry necessary
   * @return the predicate that decides weather the exception will be ignored when thrown in a retry
   */
  default boolean isIgnoredException(Exception exception) {
    return getIgnoredExceptions().stream().noneMatch(ignored -> ignored.isInstance(exception));
  }

  /**
   * This determines if a given answer is acceptable. In that case the retrying will be stopped and
   * the answer is returned.
   *
   * <p>By default this will accept
   *
   * <ul>
   *   <li>a {@link Optional#isPresent()},
   *   <li>a !{@link Collection#isEmpty()},
   *   <li>a !{@link Map#isEmpty()},
   *   <li>an array with length &gt; 0, and
   *   <li>a {@link Boolean#TRUE}.
   * </ul>
   *
   * @param answer an answer
   * @return {@code true} if the given answer is acceptable
   */
  default boolean acceptable(A answer) {
    if (answer instanceof Optional<?> optionalAnswer) {
      return optionalAnswer.isPresent();
    } else if (answer instanceof Collection<?> collectionAnswer) {
      return !collectionAnswer.isEmpty();
    } else if (answer instanceof Map<?, ?> mapAnswer) {
      return !mapAnswer.isEmpty();
    } else if (answer instanceof Object[] arrayAnswer) {
      return arrayAnswer.length > 0;
    } else if (answer instanceof Boolean booleanAnswer) {
      return booleanAnswer;
    }
    return answer != null;
  }
}
