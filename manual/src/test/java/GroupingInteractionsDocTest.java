import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.Task;
import org.shakespeareframework.selenium.BrowseTheWeb;
import org.shakespeareframework.selenium.BrowserType;
import org.shakespeareframework.selenium.LocalWebDriverSupplier;

class GroupingInteractionsDocTest {

  static String LOGIN_FORM_HTML =
      """
      <html>
        <body>
          <form class="login" action="/login" method="post">
            <label for"username">Username: </label>
            <input name="username" type="text"/>
            <label for"username">Password: </label>
            <input name="password" type="password"/>
            <button type="submit">Login</button>
          </form>
        </body>
      </html>
      """;

  static String LOGGED_IN_HTML =
      """
      <html>
        <body>
          <a href="/logout">Log Out</a>
        </body>
      </html>
      """;

  static MockWebServer mockWebServer = new MockWebServer();

  @BeforeAll
  static void initializeMockWebServer() throws IOException {
    mockWebServer.enqueue(
        new MockResponse().addHeader("Content-Type", "text/html").setBody(LOGIN_FORM_HTML));
    mockWebServer.enqueue(new MockResponse().setBody(LOGGED_IN_HTML));
    mockWebServer.start();
  }

  @AfterAll
  static void shutdownMockWebServer() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void act1() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "google-chrome").start().waitFor()).isEqualTo(0);
    final var browseTheWeb =
        new BrowseTheWeb(
            new LocalWebDriverSupplier(BrowserType.CHROME, new ChromeOptions().setHeadless(true)));
    // tag::create-actor[]
    var john = new Actor("John").can(browseTheWeb);
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

      webDriver.get(mockWebServer.url("/").toString()); // <1>

      webDriver
          .findElement(By.name("username")) // <2>
          .sendKeys(username); // <3>
      webDriver
          .findElement(By.name("password")) // <4>
          .sendKeys(password); // <5>
      webDriver
          .findElement(By.cssSelector(".login button")) // <6>
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

      return !webDriver.findElements(By.linkText("Log Out")).isEmpty();
    }

    @Override
    public String toString() {
      return "login state";
    }
  }
  // end::question[]
}
