package io.github.mkutz.shakespeare;

/**
 * A {@link Task} can be {@link Actor#performs performed} by an {@link Actor}.
 */
public interface Task {

    /**
     * Perform this {@link Task} as the given {@link Actor}.
     *
     * @param actor the {@link Actor} to perform this.
     */
    void performAs(Actor actor);
}
