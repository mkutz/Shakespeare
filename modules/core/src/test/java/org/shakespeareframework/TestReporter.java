package org.shakespeareframework;

import java.util.Stack;

final class TestReporter implements Reporter {

    private final Stack<Report<?>> reports = new Stack<>();

    @Override
    public void start(Task task) {
        reports.push(new Report<>(task));
    }

    @Override
    public void start(Question<?> question) {
        reports.push(new Report<>(question));
    }

    @Override
    public void retry(Exception cause) {
        reports.peek().retry();
    }

    @Override
    public void retry(Object answer) {
        reports.peek().retry();
    }

    @Override
    public void success() {
        reports.peek().success();
    }

    @Override
    public void success(Object answer) {
        reports.peek().success(answer);
    }

    @Override
    public void failure() {
        reports.peek().failure(null);
    }

    @Override
    public void failure(Exception cause) {
        reports.peek().failure(cause);
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
