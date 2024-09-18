package org.shakespeareframework;

import org.jspecify.annotations.NullMarked;

/**
 * A {@link MissingAbilityException} is thrown in case an {@link Actor} cannot {@link Actor#uses} a
 * required {@link Ability} {@link Class}.
 */
@NullMarked
public class MissingAbilityException extends RuntimeException {

  /**
   * @param actor the {@link Actor} missing the abilityClass
   * @param abilityClass the class of the missing {@link Ability}
   */
  public MissingAbilityException(Actor actor, Class<? extends Ability> abilityClass) {
    super(String.format("%s is not able to %s", actor, abilityClass.getSimpleName()));
  }
}
