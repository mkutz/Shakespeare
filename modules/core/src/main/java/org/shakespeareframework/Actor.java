package org.shakespeareframework;

import java.security.SecureRandom;
import java.util.*;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
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
     * A {@link List} of {@link Reporter}s the {@link Actor} informs in the lists order.
     */
    private final List<Reporter> reporters = new ArrayList<>();

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
        reporters.forEach(reporter -> reporter.start(this, task));
        try {
            task.performAs(this);
        } catch (Exception e) {
            reporters.forEach(reporter -> reporter.failure(this, e));
            throw e;
        }
        reporters.forEach(reporter -> reporter.success(this));
        return this;
    }

    /**
     * @param task the {@link RetryableTask} to be performed by this {@link Actor}
     * @return this {@link Actor}
     * @throws TimeoutException if no acceptable answer is given when the question's timeout is reached
     */
    public Actor does(RetryableTask task) {
        reporters.forEach(reporter -> reporter.start(this, task));

        final var timeout = task.getTimeout();
        final var intervalMillis = task.getInterval().toMillis();
        final var end = now().plus(timeout);

        Exception lastException;

        while (true) {
            try {
                task.performAs(this);
                reporters.forEach(reporter -> reporter.success(this));
                return this;
            } catch (Exception e) {
                lastException = e;
                if (task.getAcknowledgedExceptions().stream().anyMatch(acknowledge -> acknowledge.isInstance(e))) {
                    reporters.forEach(reporter -> reporter.failure(this, e));
                    throw e;
                }
                reporters.forEach(reporter -> reporter.retry(this, e));
            }

            if (now().isAfter(end)) {
                TimeoutException timeoutException = new TimeoutException(this, task, lastException);
                reporters.forEach(reporter -> reporter.failure(this, timeoutException));
                throw timeoutException;
            }

            try {
                MILLISECONDS.sleep(intervalMillis);
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
        reporters.forEach(reporter -> reporter.start(this, question));
        try {
            final var answer = question.answerAs(this);
            reporters.forEach(reporter -> reporter.success(this, answer));
            return answer;
        } catch (Exception e) {
            reporters.forEach(reporter -> reporter.failure(this, e));
            throw e;
        }
    }

    /**
     * @param question the {@link RetryableQuestion} to be answered by this {@link Actor}
     * @param <A>      the {@link Class} of the answer
     * @return the answer to the given Question
     * @throws TimeoutException if no acceptable answer is given when the question's timeout is reached
     */
    public <A> A checks(RetryableQuestion<A> question) {
        reporters.forEach(reporter -> reporter.start(this, question));

        final var timeout = question.getTimeout();
        final var intervalMillis = question.getInterval().toMillis();
        final var end = now().plus(timeout);

        Exception lastException;
        A lastAnswer;

        while (true) {
            try {
                final A answer = question.answerAs(this);

                lastAnswer = answer;
                lastException = null;

                if (question.acceptable(lastAnswer)) {
                    reporters.forEach(reporter -> reporter.success(this, answer));
                    return answer;
                }
                reporters.forEach(reporter -> reporter.retry(this, answer));
            } catch (Exception e) {
                lastException = e;
                if (question.getIgnoredExceptions().stream().noneMatch(ignore -> ignore.isInstance(e))) {
                    reporters.forEach(reporter -> reporter.failure(this, e));
                    throw e;
                }
            }

            if (now().isAfter(end)) {
                TimeoutException timeoutException = new TimeoutException(this, question, lastException);
                reporters.forEach(reporter -> reporter.failure(this, timeoutException));
                throw timeoutException;
            }

            try {
                MILLISECONDS.sleep(intervalMillis);
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
     * @return the {@link Fact} instance for the {@link Actor}'s {@link #facts}
     * @throws MissingFactException if there's no instance of the requested {@link Fact} {@link Class} in the
     *                              {@link Actor}'s {@link #facts}
     */
    public <F extends Fact> F remembers(Class<F> factClass) {
        return Optional.ofNullable(facts.get(factClass))
                .map(factClass::cast)
                .orElseThrow(() -> new MissingFactException(this, factClass));
    }

    /**
     * @param reporters {@link Reporter}s that should be informed
     * @return the {@link Actor}
     */
    public Actor informs(Reporter... reporters) {
        Collections.addAll(this.reporters, reporters);
        return this;
    }

    /**
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return format("%s", name);
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
