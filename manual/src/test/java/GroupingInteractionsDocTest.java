import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.Task;
import org.shakespeareframework.selenium.BrowseTheWeb;
import org.shakespeareframework.selenium.BrowserType;
import org.shakespeareframework.selenium.DockerWebDriverSupplier;

import static org.assertj.core.api.Assertions.assertThat;

class GroupingInteractionsDocTest {

    // tag::actor[]
    Actor user = new Actor()
            .can(new BrowseTheWeb(new DockerWebDriverSupplier(BrowserType.CHROME)));
    // end::actor[]

    // tag::test[]
    @Test
    void act1() {
        user.does(new Login("john", "demo"));
        assertThat(user.checks(new LoggedInState())).isTrue();
    }
    // end::test[]

    static // tag::task[]
    class Login implements Task {

        private final String username;
        private final String password;

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void performAs(Actor actor) {
            final var webDriver = actor.uses(BrowseTheWeb.class)
                    .getWebDriver();

            webDriver.get("http://parabank.parasoft.com/"); // <1>

            webDriver.findElement(By.name("username")) // <2>
                    .sendKeys(username); // <3>
            webDriver.findElement(By.name("password")) // <4>
                    .sendKeys(password); // <5>
            webDriver.findElement(By.cssSelector(".login input.button")) // <6>
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
            final var webDriver = actor.uses(BrowseTheWeb.class)
                    .getWebDriver();

            return webDriver.findElement(By.linkText("Log Out"))
                    .isDisplayed();
        }

        @Override
        public String toString() {
            return "login state";
        }
    }
    // end::question[]
}
