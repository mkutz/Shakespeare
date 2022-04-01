import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;

class ActorDocTest {

  @Test
  void act1() {
    // tag::create-actor[]
    var robin = new Actor("Robin");
    // end::create-actor[]
    assertThat(robin.getName()).isEqualTo("Robin");
  }

  @Test
  void act2() {
    // tag::create-actor-random-name[]
    var user = new Actor();
    // end::create-actor-random-name[]
    assertThat(user.getName()).isNotBlank();
  }
}
