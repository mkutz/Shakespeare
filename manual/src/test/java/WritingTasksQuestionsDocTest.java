import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.Task;
import org.shakespeareframework.selenium.BrowseTheWeb;
import org.shakespeareframework.selenium.BrowserType;
import org.shakespeareframework.selenium.DockerWebDriverSupplier;

import static org.assertj.core.api.Assertions.assertThat;

class WritingTasksQuestionsDocTest {

    // tag::actor[]
    Actor user = new Actor()
            .can(new BrowseTheWeb(new DockerWebDriverSupplier(BrowserType.CHROME)));
    // end::actor[]

    // tag::test[]
    @Test
    void act1() {
        user.does(new Login("my-login", "p4$$w0rd"));
        assertThat(user.checks(new LoggedState())).isTrue();
    }
    // end::test[]

    // tag::task[]
    class Login implements Task {

        private final String username; // <1>
        private final String password; // <1>

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void performAs(Actor actor) {
            final var webDriver = actor.uses(BrowseTheWeb.class)
                    .getWebDriver();

            webDriver.get("http://parabank.parasoft.com/");

            webDriver.findElement(By.name("username"))
                    .sendKeys(username);
            webDriver.findElement(By.name("password"))
                    .sendKeys(password);
            webDriver.findElement(By.cssSelector(".login input.button"))
                    .click();
        }

        @Override
        public String toString() {
            return String.format("login as %s", username);
        }
    }
    // end::task[]

    // tag::question[]
    class LoggedState implements Question<Boolean> {

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
