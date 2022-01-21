import org.junit.jupiter.api.Test;
import org.shakespeareframework.Ability;
import org.shakespeareframework.Actor;

import java.util.logging.Logger;

class AbilityDocTest {

    // tag::create-actor[]
    Actor alex = new Actor("Alex");
    // end::create-actor[]

    @Test
    void act1() {
        alex.can(new Log(alex.getName()));
        alex.does((he) -> {
            var logger = he.uses(Log.class).getLogger();
            logger.info("Hello World");
        });
    }

    // tag::ability[]
    class Log implements Ability {

        private final Logger logger;

        Log(String name) {
            this.logger = Logger.getLogger(name);
        }

        public Logger getLogger() {
            return logger;
        }
    }
    // end::ability[]
}
