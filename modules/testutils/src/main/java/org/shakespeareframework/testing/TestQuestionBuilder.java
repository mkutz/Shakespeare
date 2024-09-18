package org.shakespeareframework.testing;

import static java.time.Duration.ofMillis;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

import java.time.Duration;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jspecify.annotations.Nullable;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.RetryableQuestion;

/**
 * Builder to create {@link Question}s or {@link RetryableQuestion}s in an at hoc fashion.
 *
 * @param <A> type of the answer
 */
public class TestQuestionBuilder<A> {

  private static int counter = 0;
  private String string = String.format("test question #%d", ++counter);
  private Function<Actor, @Nullable A> answer = actor -> null;
  private Duration timeout = ofMillis(100);
  private Duration interval = ofMillis(10);
  private Set<Class<? extends Exception>> ignoredExceptions = Set.of();
  private Predicate<A> acceptable = givenAnswer -> true;

  /**
   * Sets the {@link RetryableQuestion#getTimeout() timeout}.
   *
   * @param timeout the desired timeout.
   * @return this {@link TestQuestionBuilder}
   */
  public TestQuestionBuilder<A> timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * Sets the {@link RetryableQuestion#getInterval() interval}.
   *
   * @param interval the desired interval
   * @return this {@link TestQuestionBuilder}
   */
  public TestQuestionBuilder<A> interval(Duration interval) {
    this.interval = interval;
    return this;
  }

  /**
   * Sets the {@link RetryableQuestion#getIgnoredExceptions() acknowledged exceptions}.
   *
   * @param ignoredExceptions the {@link Class}es of {@link Exception} to be ignored
   * @return this {@link TestQuestionBuilder}
   */
  @SafeVarargs
  public final TestQuestionBuilder<A> ignoredExceptions(
      Class<? extends Exception>... ignoredExceptions) {
    this.ignoredExceptions = stream(ignoredExceptions).collect(toSet());
    return this;
  }

  /**
   * Sets the {@link RetryableQuestion#acceptable(Object) acceptable method}.
   *
   * @param acceptable the desired method
   * @return this {@link TestQuestionBuilder}
   */
  public TestQuestionBuilder<A> acceptable(Predicate<A> acceptable) {
    this.acceptable = acceptable;
    return this;
  }

  /**
   * Sets the string returned by the {@link Question}'s {@link Object#toString() toString method}.
   *
   * @param string the desired string.
   * @return this {@link TestQuestionBuilder}
   */
  public TestQuestionBuilder<A> string(String string) {
    this.string = string;
    return this;
  }

  /**
   * Sets the {@link Question#answerAs(Actor) performAs method} method.
   *
   * @param answer the desired method
   * @return this {@link TestQuestionBuilder}
   */
  public TestQuestionBuilder<A> answer(Function<Actor, A> answer) {
    this.answer = answer;
    return this;
  }

  /**
   * Builds an anonymous {@link Question} according to the current settings.
   *
   * @return the built {@link Question}
   */
  public Question<A> build() {
    return new Question<>() {

      @Override
      public A answerAs(Actor actor) {
        return answer.apply(actor);
      }

      @Override
      public String toString() {
        return string;
      }
    };
  }

  /**
   * Builds an anonymous {@link RetryableQuestion} according to the current settings.
   *
   * @return the build {@link RetryableQuestion}
   */
  public RetryableQuestion<A> buildRetryable() {
    return new RetryableQuestion<>() {

      @Override
      public A answerAs(Actor actor) {
        return answer.apply(actor);
      }

      @Override
      public boolean acceptable(A answer) {
        return acceptable.test(answer);
      }

      @Override
      public Duration getTimeout() {
        return timeout;
      }

      @Override
      public Duration getInterval() {
        return interval;
      }

      @Override
      public Set<Class<? extends Exception>> getIgnoredExceptions() {
        return ignoredExceptions;
      }

      @Override
      public String toString() {
        return string;
      }
    };
  }
}
