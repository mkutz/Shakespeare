package io.github.mkutz.shakespeare;

import java.util.Optional;

/**
 * A {@link Fact} to be {@link Actor#learns learned} and {@link Actor#remembers remembered} by an {@link Actor}.
 */
public interface Fact {

    /**
     * @param factClass the {@link Fact} {@link Class} whose default should be returned
     * @param <F>       the type of the required {@link Fact}
     * @return the default for the given factClass,
     */
    static <F extends Fact> Optional<F> getStaticDefault(Class<F> factClass) {
        try {
            return Optional.of(factClass.getField("DEFAULT").get(null))
                    .map(factClass::cast);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return Optional.empty();
        }
    }
}
