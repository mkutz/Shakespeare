package io.github.mkutz.shakespeare;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An {@link Actor} is the central class of the Shakespeare Framework. It is basically used for any interaction with the
 * system under test.
 */
@EqualsAndHashCode
@ToString
public class Actor {

    /**
     * The {@link Set} of the {@link Actor}'s {@link Ability}s.
     */
    private final Set<Ability> abilities = new HashSet<>();

    /**
     * The {@link Set} of the {@link Fact}s the {@link Actor} remembers.
     */
    private final Set<Fact> facts = new HashSet<>();

    /**
     * @param task the {@link Task} to be performed by this {@link Actor}
     * @return this {@link Actor}
     */
    public Actor perform(Task task) {
        task.performAs(this);
        return this;
    }

    /**
     * @param question the {@link Question} to be answered by this {@link Actor}
     * @param <A>      the {@link Class} of the answer
     * @return the answer to the given Question
     */
    public <A> A answer(Question<A> question) {
        return question.answerAs(this);
    }

    /**
     * @param abilities {@link Ability}s the {@link Actor} may {@link #use}
     * @return this {@link Actor}
     */
    public Actor can(Ability... abilities) {
        this.abilities.addAll(Arrays.asList(abilities));
        return this;
    }

    /**
     * @param abilityClass the {@link Ability} {@link Class} that should be used
     * @param <A>          the required {@link Ability} {@link Class}
     * @return the {@link Ability} instance from the {@link Actor}'s {@link #abilities}
     * @throws MissingAbilityException if there's no instance of the requested {@link Ability} {@link Class} in the
     *                                 {@link Actor}'s {@link #abilities}
     */
    public <A extends Ability> A use(Class<A> abilityClass) {
        return abilities.stream()
                .filter(ability -> ability.getClass().equals(abilityClass))
                .findAny()
                .map(abilityClass::cast)
                .orElseThrow(() -> new MissingAbilityException(this, abilityClass));
    }

    /**
     * @param facts {@link Fact}s the {@link Actor} {@link #remember}s
     * @return this {@link Actor}
     */
    public Actor learn(Fact... facts) {
        this.facts.addAll(Arrays.asList(facts));
        return this;
    }

    /**
     * @param factClass the {@link Fact} {@link Class} that should be remembered
     * @param <F>       the required {@link Fact} {@link Class}
     * @return the {@link Fact} instance for the {@link Actor}'s {@link #facts}
     * @throws MissingFactException if there's no instance of the requested {@link Fact} {@link Class} in the
     *                              {@link Actor}'s {@link #facts}
     */
    public <F extends Fact> F remember(Class<F> factClass) {
        return facts.stream()
                .filter(fact -> fact.getClass().equals(factClass))
                .findAny()
                .map(factClass::cast)
                .orElseThrow(() -> new MissingFactException(this, factClass));
    }
}
