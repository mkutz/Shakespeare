package examples.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.shakespeareframework.Actor
import org.shakespeareframework.Task

class TaskDocTest {

  @Test
  fun act1() {
    val tom = Actor("Tom").can(Log("Tom"))

    // tag::does-task[]
    tom.does(SayHelloWorld())
    // end::does-task[]

    assertThat(tom).isNotNull
  }

  @Test
  fun act2() {
    val alex = Actor("Alex").can(Log("Alex"))

    // tag::does-task-with-parameter[]
    alex.does(SayHello("Shakespeare"))
    // end::does-task-with-parameter[]

    assertThat(alex).isNotNull
  }

  @Test
  fun act3() {
    val mila = Actor("Mila").can(Log("Mila"))

    // tag::does-lambda[]
    mila.does { she -> she.uses(Log::class.java).logger.info("Hi") }
    // end::does-lambda[]

    assertThat(mila).isNotNull
  }

  // tag::task[]
  class SayHelloWorld() : Task {
    override fun performAs(actor: Actor) {
      val logger = actor.uses(Log::class.java).logger
      logger.info("Hello World")
    }
  }
  // end::task[]

  // tag::task-with-parameter[]
  data class SayHello(val to: String) : Task {
    override fun performAs(actor: Actor) {
      val logger = actor.uses(Log::class.java).logger
      logger.info("Hello $to")
    }
  }
  // end::task-with-parameter[]
}
