import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Task;

class TaskDocTest {

  @Test
  void act1() {
    var tom = new Actor("Tom").can(new Log("Tom"));

    // tag::does-task[]
    tom.does(new SayHelloWorld());
    // end::does-task[]

    assertThat(tom).isNotNull();
  }

  @Test
  void act2() {
    var alex = new Actor("Alex").can(new Log("Alex"));

    // tag::does-task-with-parameter[]
    alex.does(new SayHello("Shakespeare"));
    alex.does(new SayHelloAsRecord("Shakespeare"));
    // end::does-task-with-parameter[]

    assertThat(alex).isNotNull();
  }

  @Test
  void act3() {
    var mila = new Actor("Mila").can(new Log("Mila"));

    // tag::does-lambda[]
    mila.does(she -> she.uses(Log.class).getLogger().info("Hi"));
    // end::does-lambda[]

    assertThat(mila).isNotNull();
  }

  static // tag::task[]
  class SayHelloWorld implements Task {

    @Override
    public void performAs(Actor actor) {
      var logger = actor.uses(Log.class).getLogger();
      logger.info("Hello World");
    }
  }
  // end::task[]

  static // tag::task-with-parameter[]
  class SayHello implements Task {

    private final String to;

    public SayHello(String to) {
      this.to = to;
    }

    @Override
    public void performAs(Actor actor) {
      var logger = actor.uses(Log.class).getLogger();
      logger.info(String.format("Hello %s", to));
    }
  }
  // end::task-with-parameter[]

  // tag::task-as-record[]
  record SayHelloAsRecord(String to) implements Task {

    @Override
    public void performAs(Actor actor) {
      var logger = actor.uses(Log.class).getLogger();
      logger.info(String.format("Hello %s", to));
    }
  }
  // end::task-as-record[]
}
