package examples.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.shakespeareframework.Actor

class ActorDocTest {

  @Test
  fun act1() {
    // tag::create-actor[]
    val robin = Actor("Robin")
    // end::create-actor[]
    assertThat(robin.name).isEqualTo("Robin")
  }

  @Test
  fun act2() {
    // tag::create-actor-random-name[]
    val user = Actor()
    // end::create-actor-random-name[]
    assertThat(user.name).isNotBlank()
  }
}
