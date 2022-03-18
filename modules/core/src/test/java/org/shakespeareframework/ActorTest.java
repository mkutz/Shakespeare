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
        var called = new AtomicBoolean(false);
        var task = new TestTaskBuilder().perform(actor -> called.set(true));

        actor.does(task);

        assertThat(called).isTrue();
    }

    @Test
    @DisplayName("answers calls the question's answerAs")
    void answersTest1() {
        var called = new AtomicBoolean(false);
        var answer = "Answer";
        var question = new TestQuestionBuilder<String>().answer(actor -> {
            called.set(true);
            return answer;
        });

        assertThat(actor.checks(question)).isEqualTo(answer);

        assertThat(called).isTrue();
    }

    @Test
    @DisplayName("can allows the actor to use the ability")
    void canTest1() {
        class TestAbility implements Ability {}
        var ability = new TestAbility();

        assertThat(actor.can(ability).uses(TestAbility.class))
                .isEqualTo(ability);
    }

    @Test
    @DisplayName("uses throws a MissingAbilityException")
    void usesTest1() {
        class TestAbility implements Ability {}

        assertThatExceptionOfType(MissingAbilityException.class)
                .isThrownBy(() -> actor.uses(TestAbility.class));
    }

    @Test
    @DisplayName("learns makes the actor remember a fact")
    void learnsTest1() {
        class TestFact implements Fact {}
        var fact = new TestFact();

        actor.learns(fact);

        assertThat(actor.remembers(TestFact.class))
                .isEqualTo(fact);
    }

    @Test
    @DisplayName("remembers throws a MissingFactException")
    void remembersTest1() {
        class TestFact implements Fact {}

        assertThatExceptionOfType(MissingFactException.class)
                .isThrownBy(() -> actor.remembers(TestFact.class));
    }

    @Test
    @DisplayName("equals and hashCode work as expected")
    void equalsTest1() {
        var equalActor = new Actor(actor.getName());
        var unequalActor = new Actor("Jane");

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
