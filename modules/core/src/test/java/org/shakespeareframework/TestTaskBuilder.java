package org.shakespeareframework;

import java.util.function.Consumer;

public class TestTaskBuilder {

    private static int counter = 0;
    protected String string = String.format("some task #%d", ++counter);

    public TestTaskBuilder string(String string) {
        this.string = string;
        return this;
    }

    public Task perform(Consumer<Actor> perform) {
        return new Task() {
            @Override
            public void performAs(Actor actor) {
                perform.accept(actor);
            }

            @Override
            public String toString() {
                return string;
            }
        };
    }
}
