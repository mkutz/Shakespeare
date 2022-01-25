package org.shakespeareframework;

/**
 * A {@link Task} can be {@link Actor#does done} by an {@link Actor}.
 */
public interface Task {

    /**
     * Perform this {@link Task} as the given {@link Actor}.
     *
     * @param actor the {@link Actor} to perform this.
     */
    void performAs(Actor actor);
}
