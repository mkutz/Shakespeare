package io.github.mkutz.shakespeare;

/**
 * A {@link Question} can be {@link Actor#answer}ed by an {@link Actor}
 *
 * @param <A> the {@link Class} of the {@link Question}'s answer
 */
public interface Question<A> {

    /**
     * Answer this {@link Question} as the given {@link Actor}.
     *
     * @param actor the {@link Actor} to answer this {@link Question}
     * @return the answer to this {@link Question}
     */
    A answerAs(Actor actor);
}
