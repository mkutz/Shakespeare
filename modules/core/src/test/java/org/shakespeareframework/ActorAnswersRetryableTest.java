package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ActorAnswersRetryableTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("answers calls the question's answeredAs until its timeout")
    void answersRetryableTest1() {
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));
        when(retryableQuestionMock.acceptable(any())).thenReturn(false);

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checks(retryableQuestionMock));

        verify(retryableQuestionMock, atLeast(10)).answerAs(actor);
    }

    @Test
    @DisplayName("answers returns acceptable answers immediately")
    void answersRetryableTest2() {
        final var answer = new Object();
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));

        when(retryableQuestionMock.acceptable(any())).thenReturn(true);
        when(retryableQuestionMock.answerAs(actor)).thenReturn(answer);

        assertThat(actor.checks(retryableQuestionMock)).isEqualTo(answer);

        verify(retryableQuestionMock, times(1)).answerAs(actor);
    }

    @Test
    @DisplayName("answers catches ignored exceptions immediately")
    void answersRetryableTest3() {
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));

        doThrow(new IllegalStateException()).when(retryableQuestionMock).answerAs(actor);
        when(retryableQuestionMock.getIgnoredExceptions()).thenReturn(Set.of(IllegalStateException.class));

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checks(retryableQuestionMock));

        verify(retryableQuestionMock, atLeast(10)).answerAs(actor);
    }

    @Test
    @DisplayName("answers throws not ignored exceptions immediately")
    void answersRetryableTest4() {
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));

        doThrow(new IllegalStateException()).when(retryableQuestionMock).answerAs(actor);
        when(retryableQuestionMock.getIgnoredExceptions()).thenReturn(Set.of());

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> actor.checks(retryableQuestionMock));

        verify(retryableQuestionMock, times(1)).answerAs(actor);
    }
}
