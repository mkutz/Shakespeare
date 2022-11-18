package org.shakespeareframework.cucumber;

import org.shakespeareframework.Actor;
import org.shakespeareframework.Task;

public class SayName implements Task {

  @Override
  public void performAs(Actor actor) {
    System.out.println(actor.getName());
  }
}
