package examples.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.shakespeareframework.Actor

class AbilityDocTest {

  @Test
  fun act1() {
    // tag::can[]
    val anna = Actor("Anna").can(Log("Anna"))
    // end::can[]
    // tag::uses[]
    val log = anna.uses(Log::class.java)
    log.logger.info("Hello World")
    // end::uses[]
    assertThat(log).isNotNull()
  }
}
