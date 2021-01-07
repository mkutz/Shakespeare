package io.github.mkutz.shakespeare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class ActorTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("perform calls the task's performAs")
    void performTest1() {
        final var taskMock = mock(Task.class);

        actor.perform(taskMock);

        verify(taskMock, times(1)).performAs(actor);
    }

    @Test
    @DisplayName("answer calls the question's answerAs")
    void answerTest1() {
        @SuppressWarnings("unchecked") final var questionMock = (Question<String>) mock(Question.class);
        when(questionMock.answerAs(actor)).thenReturn("Answer");

        actor.answer(questionMock);

        verify(questionMock, times(1)).answerAs(actor);
    }

    @Test
    @DisplayName("can allows the actor to use the ability")
    void canTest1() {
        final var abilityMock = mock(Ability.class);

        actor.can(abilityMock);

        assertThat(actor.use(abilityMock.getClass())).isEqualTo(abilityMock);
    }

    @Test
    @DisplayName("use throws a MissingAbilityException")
    void useTest1() {
        final var abilityMock = mock(Ability.class);

        assertThatExceptionOfType(MissingAbilityException.class)
                .isThrownBy(() -> actor.use(abilityMock.getClass()));
    }

    @Test
    @DisplayName("learn makes the actor remember a fact")
    void learnTest1() {
        final var factMock = mock(Fact.class);

        actor.learn(factMock);

        assertThat(actor.remember(factMock.getClass())).isEqualTo(factMock);
    }

    @Test
    @DisplayName("learn makes the actor remember a fact")
    void rememberTest1() {
        final var factMock = mock(Fact.class);

        assertThatExceptionOfType(MissingFactException.class)
                .isThrownBy(() -> actor.remember(factMock.getClass()));
    }
}
