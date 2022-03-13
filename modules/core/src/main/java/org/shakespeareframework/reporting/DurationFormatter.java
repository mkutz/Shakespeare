package org.shakespeareframework.reporting;

import java.time.Duration;

final class DurationFormatter {

    public static final int SECONDS_PER_HOUR = 60 * 60;
    public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * 24;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int NANOS_PER_MILLI = 1_000_000;

    private DurationFormatter() {}

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

        if (stringBuilder.length() == 0) return "<1ms";

        return stringBuilder.toString();
    }
}
