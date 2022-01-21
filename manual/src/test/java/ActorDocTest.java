import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;

class ActorDocTest {

    // tag::create-actor[]
    Actor robin = new Actor("Robin");
    // end::create-actor[]

    // tag::create-actor-random-name[]
    Actor user = new Actor();
    // end::create-actor-random-name[]

    @Test
    void act1() {
        robin.does((they) -> System.out.printf("Hello World! My name is %s", they.getName()));
        user.does((they) -> System.out.printf("Hello World! My name is %s", they.getName()));
    }
}
