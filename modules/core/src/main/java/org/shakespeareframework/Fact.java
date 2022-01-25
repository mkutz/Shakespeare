package org.shakespeareframework;

import java.util.Optional;

/**
 * A {@link Fact} to be {@link Actor#learns learned} and {@link Actor#remembers remembered} by an {@link Actor}.
 */
public interface Fact {

    /**
     * Returns an {@link Optional} instance of the given {@link Fact} {@link Class} static field {@code DEFAULT}, or an
     * {@link Optional#empty()} if there is no such field.
     *
     * @param factClass the {@link Fact} {@link Class} whose static DEFAULT should be returned
     * @param <F>       the type of the required {@link Fact}
     * @return the default for the given factClass
     */
    static <F extends Fact> Optional<F> getStaticDefault(Class<F> factClass) {
        try {
            return Optional.of(factClass.getField("DEFAULT").get(null))
                    .map(factClass::cast);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new instance of the given {@link Fact} {@link Class} using its default constructor, or an
     * {@link Optional#empty()} if there is no default constructor.
     *
     * @param factClass the {@link Fact} {@link Class} whose default should be returned
     * @param <F>       the type of the required {@link Fact}
     * @return the default for the given factClass
     */
    static <F extends Fact> Optional<F> getConstructedDefault(Class<F> factClass) {
        try {
            return Optional.of(factClass.getConstructor().newInstance())
                    .map(factClass::cast);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
