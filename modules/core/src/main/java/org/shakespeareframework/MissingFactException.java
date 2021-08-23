package org.shakespeareframework;

/**
 * A {@link MissingFactException} is thrown in case an {@link Actor} cannot {@link Actor#remembers} a required
 * {@link Fact} {@link Class}.
 */
public class MissingFactException extends RuntimeException {

    /**
     * @param actor     the {@link Actor} missing the factClass
     * @param factClass the class of the missing {@link Fact}
     */
    public MissingFactException(Actor actor, Class<? extends Fact> factClass) {
        super(String.format("%s does not remember %s", actor, factClass.getSimpleName()));
    }
}
