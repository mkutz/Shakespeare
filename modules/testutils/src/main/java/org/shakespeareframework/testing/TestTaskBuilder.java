package org.shakespeareframework.testing;

import static java.time.Duration.ofMillis;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;
import org.shakespeareframework.Actor;
import org.shakespeareframework.RetryableTask;
import org.shakespeareframework.Task;

/** Builder to create {@link Task}s or {@link RetryableTask}s in an at hoc fashion. */
public class TestTaskBuilder {

  private static int counter = 0;
  private String string = String.format("test task #%d", ++counter);
  private Consumer<Actor> perform = actor -> {};
  private Duration timeout = ofMillis(100);
  private Duration interval = ofMillis(10);
  private Set<Class<? extends Exception>> acknowledgedExceptions = Set.of();

  /**
   * Sets the {@link RetryableTask#getTimeout() timeout}.
   *
   * @param timeout the desired timeout.
   * @return this {@link TestTaskBuilder}
   */
  public TestTaskBuilder timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * Sets the {@link RetryableTask#getInterval() interval}.
   *
   * @param interval the desired interval
   * @return this {@link TestTaskBuilder}
   */
  public TestTaskBuilder interval(Duration interval) {
    this.interval = interval;
    return this;
  }

  /**
   * Sets the {@link RetryableTask#getAcknowledgedExceptions() acknowledged exceptions}.
   *
   * @param acknowledgedExceptions the {@link Class}es of {@link Exception} to be acknowledged
   * @return this {@link TestTaskBuilder}
   */
  @SafeVarargs
  public final TestTaskBuilder acknowledgedExceptions(
      Class<? extends Exception>... acknowledgedExceptions) {
    this.acknowledgedExceptions = stream(acknowledgedExceptions).collect(toSet());
    return this;
  }

  /**
   * Sets the string returned by the {@link Task}'s {@link Object#toString() toString method}.
   *
   * @param string the desired string.
   * @return this {@link TestTaskBuilder}
   */
  public TestTaskBuilder string(String string) {
    this.string = string;
    return this;
  }

  /**
   * Sets the {@link Task#performAs(Actor) performAs method} method.
   *
   * @param perform the desired method
   * @return this {@link TestTaskBuilder}
   */
  public TestTaskBuilder perform(Consumer<Actor> perform) {
    this.perform = perform;
    return this;
  }

  /**
   * Creates an anonymous {@link Task} according to the current settings.
   *
   * @return a {@link Task} according to the current settings
   */
  public Task build() {
    return new Task() {
      @Override
      public void performAs(Actor actor) {
        perform.accept(actor);
      }

      @Override
      public String toString() {
        return string;
      }
    };
  }

  /**
   * Creates an anonymous {@link RetryableTask} according to the current settings.
   *
   * @return the built {@link RetryableTask}
   */
  public RetryableTask buildRetryable() {
    return new RetryableTask() {

      @Override
      public void performAs(Actor actor) {
        perform.accept(actor);
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
      public Set<Class<? extends Exception>> getAcknowledgedExceptions() {
        return acknowledgedExceptions;
      }

      @Override
      public String toString() {
        return string;
      }
    };
  }
}
