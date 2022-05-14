package org.shakespeareframework.cucumber;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Task;

public class ShakespeareSteps {

  private final Map<String, Actor> cast = new HashMap<>();

  @Given("an actor named {string}")
  public void anActorNamed(String actorName) {
    cast.put(actorName, new Actor(actorName));
  }

  @When("{actor} does {task}")
  public void actorDoes(Actor actor, Task task) {
    actor.does(task);
  }

  @ParameterType("[\\S]+")
  public Actor actor(String actorName) {
    return Objects.requireNonNull(cast.get(actorName));
  }

  @ParameterType("[\\S]+")
  public Task task(String taskClassName)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
          InstantiationException, IllegalAccessException {
    return (Task) Class.forName(taskClassName).getConstructor().newInstance();
  }
}
