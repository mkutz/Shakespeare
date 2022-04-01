import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.Task;
import org.shakespeareframework.selenium.BrowseTheWeb;
import org.shakespeareframework.selenium.BrowserType;
import org.shakespeareframework.selenium.DockerWebDriverSupplier;

class GroupingInteractionsDocTest {

  @Test
  void act1() {
    // tag::create-actor[]
    var john =
        new Actor("John").can(new BrowseTheWeb(new DockerWebDriverSupplier(BrowserType.CHROME)));
    // end::create-actor[]
    // tag::do-task[]
    john.does(new Login("john", "demo"));
    // end::do-task[]
    // tag::check-question[]
    assertThat(john.checks(new LoggedInState())).isTrue();
    // end::check-question[]
  }

  static // tag::task[]
  class Login implements Task {

    private final String username;
    private final String password;

    Login(String username, String password) {
      this.username = username;
      this.password = password;
    }

    @Override
    public void performAs(Actor actor) {
      var webDriver = actor.uses(BrowseTheWeb.class).getWebDriver();

      webDriver.get("https://parabank.parasoft.com/"); // <1>

      webDriver
          .findElement(By.name("username")) // <2>
          .sendKeys(username); // <3>
      webDriver
          .findElement(By.name("password")) // <4>
          .sendKeys(password); // <5>
      webDriver
          .findElement(By.cssSelector(".login input.button")) // <6>
          .click(); // <7>
    }

    @Override
    public String toString() {
      return String.format("login as %s", username);
    }
  }
  // end::task[]

  static // tag::question[]
  class LoggedInState implements Question<Boolean> {

    @Override
    public Boolean answerAs(Actor actor) {
      var webDriver = actor.uses(BrowseTheWeb.class).getWebDriver();

      return webDriver.findElement(By.linkText("Log Out")).isDisplayed();
    }

    @Override
    public String toString() {
      return "login state";
    }
  }
  // end::question[]
}
