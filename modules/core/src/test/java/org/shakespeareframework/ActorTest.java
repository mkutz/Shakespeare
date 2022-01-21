package org.shakespeareframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActorTest {

    Actor actor = new Actor();

    @Test
    @DisplayName("does calls the task's performAs")
    void doesTest1() {
        final var called = new AtomicBoolean(false);
        final Task task = (actor) -> called.set(true);

        actor.does(task);

        assertThat(called).isTrue();
    }

    @Test
    @DisplayName("answers calls the question's answerAs")
    void answersTest1() {
        final var called = new AtomicBoolean(false);
        final var answer = "Answer";
        final Question<String> question = (actor) -> {
            called.set(true);
            return answer;
        };

        assertThat(actor.checks(question)).isEqualTo(answer);

        assertThat(called).isTrue();
    }

    @Test
    @DisplayName("can allows the actor to use the ability")
    void canTest1() {
        final class TestAbility implements Ability {}
        final var ability = new TestAbility();

        assertThat(actor.can(ability).uses(TestAbility.class))
                .isEqualTo(ability);
    }

    @Test
    @DisplayName("uses throws a MissingAbilityException")
    void usesTest1() {
        final class TestAbility implements Ability {}

        assertThatExceptionOfType(MissingAbilityException.class)
                .isThrownBy(() -> actor.uses(TestAbility.class));
    }

    @Test
    @DisplayName("learns makes the actor remember a fact")
    void learnsTest1() {
        final class TestFact implements Fact {}
        final var fact = new TestFact();

        actor.learns(fact);

        assertThat(actor.remembers(TestFact.class))
                .isEqualTo(fact);
    }

    @Test
    @DisplayName("remembers throws a MissingFactException")
    void remembersTest1() {
        final class TestFact implements Fact {}

        assertThatExceptionOfType(MissingFactException.class)
                .isThrownBy(() -> actor.remembers(TestFact.class));
    }

    @Test
    @DisplayName("equals and hashCode work as expected")
    void equalsTest1() {
        final var equalActor = new Actor(actor.getName());
        final var unequalActor = new Actor("Jane");

        assertThat(equalActor.equals(actor))
                .isTrue();
        assertThat(unequalActor.equals(actor))
                .isFalse();

        assertThat(equalActor)
                .hasSameHashCodeAs(actor);
        assertThat(unequalActor)
                .doesNotHaveSameHashCodeAs(actor);
    }
}
