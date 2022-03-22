import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.selenium.BrowseTheWeb;
import org.shakespeareframework.selenium.BrowserType;
import org.shakespeareframework.selenium.DockerWebDriverSupplier;
import org.shakespeareframework.selenium.LocalWebDriverSupplier;
import org.shakespeareframework.selenium.WebDriverManagerWebDriverSupplier;

class SeleniumDocTest {

  @Test
  void act1() throws IOException, InterruptedException {
    assumeThat(System.getenv("CI")).isNull();
    assumeThat(new ProcessBuilder("which", "firefox").start().waitFor()).isEqualTo(0);

    // tag::local[]
    var tim =
        new Actor("Tim").can(new BrowseTheWeb(new LocalWebDriverSupplier(BrowserType.FIREFOX)));
    // end::local[]

    var latestRelease = tim.checks(new LatestShakespeareReleaseVersion());

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?");
  }

  @Test
  void act2() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "firefox").start().waitFor()).isEqualTo(0);

    // tag::additional-capabilities[]
    var cameron =
        new Actor("Cameron")
            .can(
                new BrowseTheWeb(
                    new LocalWebDriverSupplier(
                        BrowserType.FIREFOX, new FirefoxOptions().setHeadless(true))));
    // end::additional-capabilities[]

    var latestRelease = cameron.checks(new LatestShakespeareReleaseVersion());

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?");
  }

  @Test
  void act3() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0);

    // tag::docker[]
    var jodie =
        new Actor("Jodie").can(new BrowseTheWeb(new DockerWebDriverSupplier(BrowserType.FIREFOX)));
    // end::docker[]

    var latestRelease = jodie.checks(new LatestShakespeareReleaseVersion());

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?");
  }

  @Test
  void act4() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0);

    // tag::web-driver-manager[]
    var webDriverManager = WebDriverManager.edgedriver().browserInDocker();
    var alex =
        new Actor("Alex")
            .can(
                new BrowseTheWeb(
                    new WebDriverManagerWebDriverSupplier(webDriverManager, BrowserType.EDGE)));
    // end::web-driver-manager[]

    var latestRelease = alex.checks(new LatestShakespeareReleaseVersion());

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?");
  }

  // tag::example-question[]
  record LatestShakespeareReleaseVersion() implements Question<String> {

    @Override
    public String answerAs(Actor actor) {
      final var webDriver = actor.uses(BrowseTheWeb.class).getWebDriver();
      webDriver.get("https://shakespeareframework.org/latest/manual");
      return webDriver.findElement(By.id("revnumber")).getText();
    }
  }
  // tag::example-question[]
}
