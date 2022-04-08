package org.shakespeareframework.reporting;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.time.Instant.now;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import org.shakespeareframework.Question;
import org.shakespeareframework.Task;

/** Report on a {@link Task} or {@link Question}. */
class LoggingReport {

  private final String subject;
  private final Instant started;
  private final StringBuilder supplementBuilder = new StringBuilder();
  private final Deque<LoggingReport> subReports = new ArrayDeque<>();
  private Status status = Status.STARTED;
  private Duration duration;
  private int retries = 0;

  /** @param subject a description what this is about */
  LoggingReport(String subject) {
    this.subject = subject;
    this.started = now();
  }

  /**
   * Increments the {@link #retries} counter.
   *
   * @return this {@link LoggingReport}
   */
  LoggingReport retry() {
    retries++;
    return this;
  }

  LoggingReport addSupplement(String supplement) {
    supplementBuilder.append(supplement);
    return this;
  }

  LoggingReport finish() {
    duration = Duration.between(started, now());
    return this;
  }

  LoggingReport success() {
    status = Status.SUCCESS;
    return finish();
  }

  /**
   * Marks the report as finished successfully.
   *
   * @return this {@link LoggingReport}
   */
  LoggingReport success(String supplement) {
    return success().addSupplement(supplement);
  }

  /**
   * Marks the report as finished unsuccessfully.
   *
   * @return this {@link LoggingReport}
   */
  LoggingReport failure() {
    status = Status.FAILURE;
    return finish();
  }

  /**
   * Marks the report as finished unsuccessfully.
   *
   * @param supplement a final supplement to be added
   * @return this {@link LoggingReport}
   */
  LoggingReport failure(String supplement) {
    return failure().addSupplement(supplement);
  }

  /**
   * @return the {@link Duration} since the {@link LoggingReport} was created until either now if
   *     the {@link #status} is not finished yet, or the instant the report was finished.
   */
  Duration getDuration() {
    return duration == null ? Duration.between(started, now()) : duration;
  }

  /**
   * @return true, if this {@link LoggingReport} and all its {@link #subReports} are not {@link
   *     Status#STARTED started}
   */
  boolean isFinished() {
    return status != Status.STARTED
        && subReports.parallelStream().noneMatch(subReport -> subReport.status == Status.STARTED);
  }

  /**
   * @param subReport the sub {@link LoggingReport}
   * @return this {@link LoggingReport}
   */
  LoggingReport addSubReport(LoggingReport subReport) {
    this.subReports.push(subReport);
    return this;
  }

  LoggingReport getCurrentReport() {
    return subReports.stream()
        .filter(report -> report.status == Status.STARTED)
        .findFirst()
        .map(LoggingReport::getCurrentReport)
        .orElse(this);
  }

  public String toString(String prefix) {
    var subReportsString = "";
    final var subPrefix = prefix.replace('├', '│').replace('└', ' ').replace('─', ' ');
    if (!subReports.isEmpty()) {
      final var lastSubReport = subReports.getFirst();
      final var subReportsStrings =
          subReports.stream()
              .filter(report -> !report.equals(lastSubReport))
              .map(subReport -> subReport.toString(subPrefix + "├── "))
              .collect(toList());
      subReportsStrings.add(lastSubReport.toString(subPrefix + "└── "));
      subReportsString = "\n" + join("\n", subReportsStrings);
    }
    final var subjectString =
        subject.contains("\n")
            ? join(
                "\n",
                subject.lines().limit(1).collect(joining()),
                subject.lines().skip(1).map(line -> subPrefix + line).collect(joining("\n")))
            : subject;
    return format(
        "%s%s %s%c %s%s%s",
        prefix,
        subjectString,
        "•".repeat(retries),
        status.getSymbol(),
        DurationFormatter.format(getDuration()),
        supplementBuilder.length() > 0 ? " " + supplementBuilder : "",
        subReportsString);
  }

  @Override
  public String toString() {
    return toString("");
  }

  private enum Status {
    STARTED('…'),
    SUCCESS('✓'),
    FAILURE('✗');

    final char symbol;

    Status(char symbol) {
      this.symbol = symbol;
    }

    public char getSymbol() {
      return symbol;
    }
  }
}
