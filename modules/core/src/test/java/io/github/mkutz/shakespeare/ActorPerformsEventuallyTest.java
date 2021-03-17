package io.github.mkutz.shakespeare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActorPerformsEventuallyTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("performsEventually calls the task's performAs until its timeout")
    void performsEventuallyTest1() {
        final var retryableTaskMock = mock(RetryableTask.class);
        when(retryableTaskMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableTaskMock.getInterval()).thenReturn(Duration.ofMillis(10));
        doThrow(new RuntimeException()).when(retryableTaskMock).performAs(actor);

        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> actor.performsEventually(retryableTaskMock));

        verify(retryableTaskMock, atLeast(10)).performAs(actor);
    }

    @Test
    @DisplayName("performsEventually returns immediately after success")
    void performsEventuallyTest2() {
        final var retryableTaskMock = mock(RetryableTask.class);
        when(retryableTaskMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableTaskMock.getInterval()).thenReturn(Duration.ofMillis(10));

        actor.performsEventually(retryableTaskMock);

        verify(retryableTaskMock, times(1)).performAs(actor);
    }

    @Test
    @DisplayName("performsEventually throws acknowledged exceptions immediately")
    void performsEventuallyTest3() {
        final var retryableTaskMock = mock(RetryableTask.class);
        when(retryableTaskMock.getTimeout()).thenReturn(Duration.ofMillis(100));
        when(retryableTaskMock.getInterval()).thenReturn(Duration.ofMillis(10));

        doThrow(new IllegalStateException()).when(retryableTaskMock).performAs(actor);
        when(retryableTaskMock.getAcknowledgedExceptions()).thenReturn(Set.of(IllegalStateException.class));

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> actor.performsEventually(retryableTaskMock));

        verify(retryableTaskMock, times(1)).performAs(actor);
    }
}
