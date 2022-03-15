package org.shakespeareframework;

import java.util.function.Function;

public class TestQuestionBuilder<A> {

    private static int counter = 0;
    protected String string = String.format("some question #%d", ++counter);

    public TestQuestionBuilder<A> string(String string) {
        this.string = string;
        return this;
    }

    public Question<A> answer(Function<Actor, A> answer) {
        return new Question<>() {

            @Override
            public A answerAs(Actor actor) {
                return answer.apply(actor);
            }

            @Override
            public String toString() {
                return string;
            }
        };
    }
}
