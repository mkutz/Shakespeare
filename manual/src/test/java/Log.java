import org.shakespeareframework.Ability;

import java.util.logging.Logger;

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
