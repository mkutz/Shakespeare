package io.github.mkutz.shakespeare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActorAnswersEventuallyTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("answersEventually calls the question's answeredAs until its timeout")
    void answersEventuallyTest1() {
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));
        when(retryableQuestionMock.acceptable(any())).thenReturn(false);

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checksEventually(retryableQuestionMock));

        verify(retryableQuestionMock, atLeast(10)).answerAs(actor);
    }

    @Test
    @DisplayName("answersEventually returns acceptable answers immediately")
    void answersEventuallyTest2() {
        final var answer = new Object();
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));

        when(retryableQuestionMock.acceptable(any())).thenReturn(true);
        when(retryableQuestionMock.answerAs(actor)).thenReturn(answer);

        assertThat(actor.checksEventually(retryableQuestionMock)).isEqualTo(answer);

        verify(retryableQuestionMock, times(1)).answerAs(actor);
    }

    @Test
    @DisplayName("answersEventually catches ignored exceptions immediately")
    void answersEventuallyTest3() {
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));

        doThrow(new IllegalStateException()).when(retryableQuestionMock).answerAs(actor);
        when(retryableQuestionMock.getIgnoredExceptions()).thenReturn(Set.of(IllegalStateException.class));

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.checksEventually(retryableQuestionMock));

        verify(retryableQuestionMock, atLeast(10)).answerAs(actor);
    }

    @Test
    @DisplayName("answersEventually throws not ignored exceptions immediately")
    void answersEventuallyTest4() {
        final var retryableQuestionMock = mock(RetryableQuestion.class);
        when(retryableQuestionMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableQuestionMock.getInterval()).thenReturn(Duration.ofMillis(10));

        doThrow(new IllegalStateException()).when(retryableQuestionMock).answerAs(actor);
        when(retryableQuestionMock.getIgnoredExceptions()).thenReturn(Set.of());

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> actor.checksEventually(retryableQuestionMock));

        verify(retryableQuestionMock, times(1)).answerAs(actor);
    }
}
