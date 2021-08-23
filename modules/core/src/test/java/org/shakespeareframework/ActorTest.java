package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class ActorTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("performs calls the task's performAs")
    void performsTest1() {
        final var taskMock = mock(Task.class);

        actor.does(taskMock);

        verify(taskMock, times(1)).performAs(actor);
    }

    @Test
    @DisplayName("answers calls the question's answerAs")
    void answersTest1() {
        final var mockedAnswer = "Answer";
        @SuppressWarnings("unchecked") final var questionMock = (Question<String>) mock(Question.class);
        when(questionMock.answerAs(actor)).thenReturn(mockedAnswer);

        assertThat(actor.checks(questionMock)).isEqualTo(mockedAnswer);

        verify(questionMock, times(1)).answerAs(actor);
    }

    @Test
    @DisplayName("can allows the actor to use the ability")
    void canTest1() {
        final var abilityMock = mock(Ability.class);

        assertThat(actor.can(abilityMock).uses(abilityMock.getClass()))
                .isEqualTo(abilityMock);
    }

    @Test
    @DisplayName("uses throws a MissingAbilityException")
    void usesTest1() {
        final var abilityMock = mock(Ability.class);
        final var abilityClass = abilityMock.getClass();

        assertThatExceptionOfType(MissingAbilityException.class)
                .isThrownBy(() -> actor.uses(abilityClass));
    }

    @Test
    @DisplayName("learns makes the actor remember a fact")
    void learnsTest1() {
        final var factMock = mock(Fact.class);

        assertThat(actor.learns(factMock).remembers(factMock.getClass()))
                .isEqualTo(factMock);
    }

    @Test
    @DisplayName("remembers throws a MissingFactException")
    void remembersTest1() {
        final var factMock = mock(Fact.class);
        final var factClass = factMock.getClass();

        assertThatExceptionOfType(MissingFactException.class)
                .isThrownBy(() -> actor.remembers(factClass));
    }
}
