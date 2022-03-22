package org.shakespeareframework.reporting;

import java.util.Stack;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.RetryableQuestion;
import org.shakespeareframework.RetryableTask;
import org.shakespeareframework.Task;

final class TestReporter implements Reporter {

  private final Stack<Report<?>> reports = new Stack<>();

  @Override
  public void start(Actor actor, Task task) {
    reports.push(new Report<>(task));
  }

  @Override
  public void start(Actor actor, Question<?> question) {
    reports.push(new Report<>(question));
  }

  @Override
  public void retry(Actor actor, RetryableTask task, Exception cause) {
    reports.peek().retry();
  }

  @Override
  public <A> void retry(Actor actor, RetryableQuestion<A> question, A answer) {
    reports.peek().retry();
  }

  @Override
  public void retry(Actor actor, RetryableQuestion<?> question, Exception cause) {
    reports.peek().retry();
  }

  @Override
  public void success(Actor actor, Task task) {
    reports.peek().success();
  }

  @Override
  public <A> void success(Actor actor, Question<A> question, A answer) {
    reports.peek().success(answer);
  }

  @Override
  public void failure(Actor actor, Task task, Exception cause) {
    reports.peek().failure(cause);
  }

  @Override
  public void failure(Actor actor, Question<?> question, Exception cause) {
    reports.peek().failure(cause);
  }

  @Override
  public <A> void failure(Actor actor, Question<A> question, A answer) {
    reports.peek().failure(answer);
  }

  public Stack<Report<?>> getReports() {
    return reports;
  }

  static class Report<T> {

    private final T subject;
    private int retries = 0;
    private Boolean success = null;
    private Exception cause = null;
    private Object answer = null;

    public Report(T subject) {
      this.subject = subject;
    }

    private void retry() {
      retries++;
    }

    public void success() {
      this.success = true;
    }

    public void success(Object answer) {
      this.success = true;
      this.answer = answer;
    }

    public void failure(Exception cause) {
      this.success = false;
      this.cause = cause;
    }

    public void failure(Object answer) {
      this.success = false;
      this.answer = answer;
    }

    public T getSubject() {
      return subject;
    }

    public int getRetries() {
      return retries;
    }

    public Boolean isSuccess() {
      return success;
    }

    public Exception getCause() {
      return cause;
    }

    public Object getAnswer() {
      return answer;
    }
  }
}
