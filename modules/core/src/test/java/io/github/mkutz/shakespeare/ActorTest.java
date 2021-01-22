package io.github.mkutz.shakespeare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActorTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("perform calls the task's performAs")
    void performsTest1() {
        final var taskMock = mock(Task.class);

        actor.performs(taskMock);

        verify(taskMock, times(1)).performAs(actor);
    }

    @Test
    @DisplayName("answer calls the question's answerAs")
    void answersTest1() {
        @SuppressWarnings("unchecked") final var questionMock = (Question<String>) mock(Question.class);
        when(questionMock.answerAs(actor)).thenReturn("Answer");

        actor.answers(questionMock);

        verify(questionMock, times(1)).answerAs(actor);
    }

    @Test
    @DisplayName("can allows the actor to use the ability")
    void canTest1() {
        final var abilityMock = mock(Ability.class);

        actor.can(abilityMock);

        assertThat(actor.uses(abilityMock.getClass())).isEqualTo(abilityMock);
    }

    @Test
    @DisplayName("use throws a MissingAbilityException")
    void usesTest1() {
        final var abilityMock = mock(Ability.class);
        final var abilityClass = abilityMock.getClass();

        assertThatExceptionOfType(MissingAbilityException.class)
                .isThrownBy(() -> actor.uses(abilityClass));
    }

    @Test
    @DisplayName("learn makes the actor remember a fact")
    void learnsTest1() {
        final var factMock = mock(Fact.class);

        actor.learns(factMock);

        assertThat(actor.remembers(factMock.getClass())).isEqualTo(factMock);
    }

    @Test
    @DisplayName("learn makes the actor remember a fact")
    void remembersTest1() {
        final var factMock = mock(Fact.class);
        final var factClass = factMock.getClass();

        assertThatExceptionOfType(MissingFactException.class)
                .isThrownBy(() -> actor.remembers(factClass));
    }
}
