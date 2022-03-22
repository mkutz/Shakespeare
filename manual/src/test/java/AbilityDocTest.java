import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;

class AbilityDocTest {

  @Test
  void act1() {
    // tag::can[]
    var anna = new Actor("Anna").can(new Log("Anna"));
    // end::can[]
    // tag::uses[]
    var log = anna.uses(Log.class);
    log.getLogger().info("Hello World");
    // end::uses[]
    assertThat(log).isNotNull();
  }
}
