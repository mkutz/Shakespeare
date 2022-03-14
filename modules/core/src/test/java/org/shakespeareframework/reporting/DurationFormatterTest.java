package org.shakespeareframework.reporting;

import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.time.api.constraints.DurationRange;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.shakespeareframework.reporting.DurationFormatter.format;

class DurationFormatterTest {

    @Property
    @Label("format returns a valid string for any duration")
    void test1(@ForAll @DurationRange Duration duration) {
        assertThat(format(duration))
                .matches("^((\\d+d)?(\\d+h)?(\\d+m)?(\\d+s)?(\\d+ms)?|<1ms)$");
    }

    @ParameterizedTest(name = "{0}")
    @EnumSource(FormattedDurationExamples.class)
    void test2(FormattedDurationExamples example) {
        assertThat(format(example.duration))
                .isEqualTo(example.formatted);
    }

    private enum FormattedDurationExamples {

        ZERO(Duration.ofSeconds(0), "<1ms"),
        NANOS_999(Duration.ofNanos(999), "<1ms"),
        MILLIS_1(Duration.ofMillis(1), "1ms"),
        SECONDS_1(Duration.ofSeconds(1), "1s"),
        SECONDS_90(Duration.ofSeconds(90), "1m30s"),
        MINUTES_1(Duration.ofMinutes(1), "1m"),
        HOURS_1(Duration.ofHours(1), "1h"),
        DAYS_1(Duration.ofDays(1), "1d"),
        SECONDS_4_MILLIS_999(Duration.ofSeconds(4).plusMillis(999), "4s999ms"),
        SECONDS_5_MILLIS_999(Duration.ofSeconds(5).plusMillis(999), "5s"),
        MINUTES_1_SECONDS_2_MILLIS_3(Duration.ofMinutes(1).plusSeconds(2).plusMillis(3), "1m2s"),
        DAYS_32_HOURS_23_MINUTES_59_SECONDS_59_MILLIS_999(Duration.ofDays(32).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999), "32d23h59m59s");

        final Duration duration;
        final String formatted;

        FormattedDurationExamples(Duration duration, String formatted) {
            this.duration = duration;
            this.formatted = formatted;
        }

        @Override
        public String toString() {
            return String.format("Duration of %s is formatted to \"%s\"",
                    name().toLowerCase().replace('_', ' '),
                    formatted);
        }
    }
}
