package org.shakespeareframework;

import java.security.SecureRandom;
import java.util.*;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * An {@link Actor} is the central class of the Shakespeare Framework. It is basically used for any interaction with the
 * system under test.
 */
public final class Actor {

    private static final String[] NAMES = {
            "Alex", "Morgan", "Robin", "Sam", "Max", "Ryan",
            "Johnny", "Arnold", "Jim", "Daniel", "Leonardo", "Tom",
            "Emma", "Cameron", "Kate", "Natalie", "Angelina", "Scarlett"
    };

    private final String name;

    /**
     * A {@link Map} of {@link Ability}s the {@link Actor}'s posses.
     */
    private final Map<Class<? extends Ability>, Ability> abilities = new HashMap<>();

    /**
     * A {@link Map} of the {@link Fact}s the {@link Actor} remembers.
     */
    private final Map<Class<? extends Fact>, Fact> facts = new HashMap<>();

    /**
     * @param name a name used for logging and reporting
     */
    public Actor(String name) {
        this.name = requireNonNull(name);
    }

    /**
     * Picks a name from {@link #NAMES}.
     */
    public Actor() {
        this(NAMES[new SecureRandom().nextInt(NAMES.length)]);
    }

    /**
     * @param task the {@link Task} to be performed by this {@link Actor}
     * @return this {@link Actor}
     */
    public Actor does(Task task) {
        task.performAs(this);
        return this;
    }

    /**
     * @param task the {@link RetryableTask} to be performed by this {@link Actor}
     * @return this {@link Actor}
     * @throws TimeoutException if no acceptable answer is given when the question's timeout is reached
     */
    public Actor doesEventually(RetryableTask task) {
        final var timeout = task.getTimeout();
        final var intervalMillis = task.getInterval().toMillis();
        final var end = now().plus(timeout);

        Exception lastException;

        while (true) {
            try {
                task.performAs(this);
                return this;
            } catch (Exception e) {
                lastException = e;
                if (task.getAcknowledgedExceptions().stream().anyMatch(acknowledge -> acknowledge.isInstance(e))) {
                    throw e;
                }
            }

            if (now().isAfter(end)) {
                throw new TimeoutException(this, task, lastException);
            }

            try {
                sleep(intervalMillis);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                throw new RetryInterruptedException(this, task, e);
            }
        }
    }

    /**
     * @param question the {@link Question} to be answered by this {@link Actor}
     * @param <A>      the {@link Class} of the answer
     * @return the answer to the given Question
     */
    public <A> A checks(Question<A> question) {
        return question.answerAs(this);
    }

    /**
     * @param question the {@link RetryableQuestion} to be answered by this {@link Actor}
     * @param <A>      the {@link Class} of the answer
     * @return the answer to the given Question
     * @throws TimeoutException if no acceptable answer is given when the question's timeout is reached
     */
    public <A> A checksEventually(RetryableQuestion<A> question) {
        final var timeout = question.getTimeout();
        final var intervalMillis = question.getInterval().toMillis();
        final var end = now().plus(timeout);

        Exception lastException;
        A lastAnswer;

        while (true) {
            try {
                lastAnswer = question.answerAs(this);
                lastException = null;

                if (question.acceptable(lastAnswer)) {
                    return lastAnswer;
                }
            } catch (Exception e) {
                lastException = e;
                if (question.getIgnoredExceptions().stream().noneMatch(ignore -> ignore.isInstance(e))) {
                    throw e;
                }
            }

            if (now().isAfter(end)) {
                throw new TimeoutException(this, question, lastException);
            }

            try {
                sleep(intervalMillis);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                throw new RetryInterruptedException(this, question, e);
            }
        }
    }

    /**
     * @param abilities {@link Ability}s the {@link Actor} may {@link #uses}
     * @return this {@link Actor}
     */
    public Actor can(Ability... abilities) {
        this.abilities.putAll(Arrays.stream(abilities).collect(toMap(Ability::getClass, identity())));
        return this;
    }

    /**
     * @param abilityClass the {@link Ability} {@link Class} that should be used
     * @param <A>          the required {@link Ability} {@link Class}
     * @return the {@link Ability} instance from the {@link Actor}'s {@link #abilities}
     * @throws MissingAbilityException if there's no instance of the requested {@link Ability} {@link Class} in the
     *                                 {@link Actor}'s {@link #abilities}
     */
    public <A extends Ability> A uses(Class<A> abilityClass) {
        return Optional.ofNullable(abilities.get(abilityClass))
                .map(abilityClass::cast)
                .orElseThrow(() -> new MissingAbilityException(this, abilityClass));
    }

    /**
     * @param facts {@link Fact}s the {@link Actor} {@link #remembers}s
     * @return this {@link Actor}
     */
    public Actor learns(Fact... facts) {
        this.facts.putAll(Arrays.stream(facts).collect(toMap(Fact::getClass, identity())));
        return this;
    }

    /**
     * @param factClass the {@link Fact} {@link Class} that should be remembered
     * @param <F>       the required {@link Fact} {@link Class}
     * @return an {@link Optional} {@link Fact} from the {@link Actor}'s {@link #facts}
     */
    public <F extends Fact> Optional<F> remembersOptional(Class<F> factClass) {
        return Optional.ofNullable(facts.get(factClass))
                .or(() -> Fact.getStaticDefault(factClass))
                .or(() -> Fact.getConstructedDefault(factClass))
                .map(factClass::cast);
    }

    /**
     * @param factClass the {@link Fact} {@link Class} that should be remembered
     * @param <F>       the required {@link Fact} {@link Class}
     * @return the {@link Fact} instance from the {@link Actor}'s {@link #facts} or the factClass' DEFAULT field
     * @throws MissingFactException if there's no instance of the requested {@link Fact} {@link Class} in the
     *                              {@link Actor}'s {@link #facts} and there is no static DEFAULT
     */
    public <F extends Fact> F remembers(Class<F> factClass) {
        return remembersOptional(factClass)
                .orElseThrow(() -> new MissingFactException(this, factClass));
    }

    /**
     * @param factClass the {@link Fact} {@link Class} that should be remembered
     * @param <F>       the required {@link Fact} {@link Class}
     * @return the {@link Fact} instance from the {@link Actor}'s {@link #facts} or the given alternative
     */
    public <F extends Fact> F remembers(Class<F> factClass, F alternative) {
        return remembersOptional(factClass)
                .orElse(alternative);
    }

    @Override
    public String toString() {
        return "%s".formatted(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Actor) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.abilities, that.abilities) &&
                Objects.equals(this.facts, that.facts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, abilities, facts);
    }
}
