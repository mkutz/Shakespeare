package org.shakespeareframework.reporting;

import java.time.Duration;
import org.jspecify.annotations.NullMarked;

/** Utility class to format {@link Duration}s as {@link String}s. */
@NullMarked
final class DurationFormatter {

  public static final int SECONDS_PER_HOUR = 60 * 60;
  public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * 24;
  public static final int SECONDS_PER_MINUTE = 60;
  public static final int NANOS_PER_MILLI = 1_000_000;

  /** Private constructor as utility classes should not be instantiated. */
  private DurationFormatter() {}

  /**
   * Formats the given {@link Duration} as a String with time unit abbreviations for days (d), hours
   * (h), minutes (m), seconds (s) and millis (ms).
   *
   * <p>Durations of less than 1ms will simply return &lt;1ms.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>1 day = 1d
   *   <li>1½ hours = 1h30m
   *   <li>46 seconds = 46s
   *   <li>90 seconds = 1m30s
   *   <li>999 millis = 999ms
   *   <li>1½ seconds = 1s500ms
   *   <li>999 nanos = &lt;1ms
   * </ul>
   *
   * @param duration the Duration to be formatted.
   * @return the String representation of the given Duration.
   */
  public static String format(Duration duration) {
    final var stringBuilder = new StringBuilder();
    final long seconds = duration.getSeconds();
    final long d = seconds / SECONDS_PER_DAY;
    final long h = (seconds % SECONDS_PER_DAY / SECONDS_PER_HOUR);
    final long m = (seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
    final long s = seconds % SECONDS_PER_MINUTE;
    if (d > 0) stringBuilder.append(d).append('d');
    if (h > 0) stringBuilder.append(h).append('h');
    if (m > 0) stringBuilder.append(m).append('m');
    if (s > 0) stringBuilder.append(s).append('s');

    if (seconds < 5) {
      final int nanos = duration.getNano();
      final int ms = nanos / NANOS_PER_MILLI;
      if (ms > 0) stringBuilder.append(ms).append("ms");
    }

    if (stringBuilder.isEmpty()) return "<1ms";

    return stringBuilder.toString();
  }
}
