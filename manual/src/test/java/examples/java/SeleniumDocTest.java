package examples.java;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.selenium.BrowseTheWeb;
import org.shakespeareframework.selenium.BrowserType;
import org.shakespeareframework.selenium.HtmlSnapshotReporter;
import org.shakespeareframework.selenium.LocalWebDriverSupplier;
import org.shakespeareframework.selenium.ScreenshotReporter;
import org.shakespeareframework.selenium.WebDriverManagerWebDriverSupplier;

@Execution(ExecutionMode.SAME_THREAD)
class SeleniumDocTest {

  @Test
  void act1() throws IOException, InterruptedException {
    assumeThat(System.getenv("CI")).isNull();
    assumeThat(new ProcessBuilder("which", "google-chrome").start().waitFor()).isEqualTo(0);

    // tag::local[]
    var tim =
        new Actor("Tim").can(new BrowseTheWeb(new LocalWebDriverSupplier(BrowserType.CHROME)));
    // end::local[]

    var latestRelease = tim.checks(new LatestShakespeareReleaseVersion());

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?");
  }

  @Test
  void act2() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "google-chrome").start().waitFor()).isEqualTo(0);

    // tag::additional-capabilities[]
    var cameron =
        new Actor("Cameron")
            .can(
                new BrowseTheWeb(
                    new LocalWebDriverSupplier(
                        BrowserType.CHROME, new ChromeOptions().addArguments("--headless"))));
    // end::additional-capabilities[]

    var latestRelease = cameron.checks(new LatestShakespeareReleaseVersion());

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?");
  }

  @Test
  @Disabled("fails with SessionNotCreatedException for no obvious reason")
  void act3() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0);

    // tag::web-driver-manager[]
    var webDriverManager = WebDriverManager.edgedriver().browserInDocker();
    var alex =
        new Actor("Alex")
            .can(
                new BrowseTheWeb(
                    new WebDriverManagerWebDriverSupplier(webDriverManager, BrowserType.CHROME)));
    // end::web-driver-manager[]

    var latestRelease = alex.checks(new LatestShakespeareReleaseVersion());

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?");
  }

  @Test
  void act4() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0);
    try (var webDriverSupplier =
        new WebDriverManagerWebDriverSupplier(
            WebDriverManager.chromedriver().browserInDocker(), BrowserType.CHROME)) {
      // tag::open-base-url[]
      var browseTheWeb = new BrowseTheWeb(webDriverSupplier, "https://shakespeareframework.org/");

      assertThat(browseTheWeb.getWebDriver().getCurrentUrl())
          .isEqualTo("https://shakespeareframework.org/");
      // end::open-base-url[]
    }
  }

  @Test
  void act5() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0);

    // tag::screenshot-reporter[]
    var reportsPath = Path.of("build", "reports", "shakespeare");
    var imogen =
        new Actor("Imogen")
            .can(
                new BrowseTheWeb(
                    new WebDriverManagerWebDriverSupplier(
                        WebDriverManager.firefoxdriver().browserInDocker(), BrowserType.FIREFOX)))
            .informs(new ScreenshotReporter(reportsPath, true));

    imogen.checks(new LatestShakespeareReleaseVersion());

    assertThat(reportsPath.resolve("001-imogen-success-latest_shakespeare_release_version.png"))
        .isNotEmptyFile();
    // end::screenshot-reporter[]
  }

  @Test
  void act6() throws IOException, InterruptedException {
    assumeThat(new ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0);

    // tag::html-snapshot-reporter[]
    var reportsPath = Path.of("build", "reports", "shakespeare");
    var tim =
        new Actor("Tim")
            .can(
                new BrowseTheWeb(
                    new WebDriverManagerWebDriverSupplier(
                        WebDriverManager.firefoxdriver().browserInDocker(), BrowserType.FIREFOX)))
            .informs(new HtmlSnapshotReporter(reportsPath, true));

    tim.checks(new LatestShakespeareReleaseVersion());

    assertThat(reportsPath.resolve("001-tim-success-latest_shakespeare_release_version.html"))
        .isNotEmptyFile();
    // end::html-snapshot-reporter[]
  }

  // tag::example-question[]
  record LatestShakespeareReleaseVersion() implements Question<String> {

    @Override
    public String answerAs(Actor actor) {
      final var webDriver = actor.uses(BrowseTheWeb.class).getWebDriver();
      webDriver.get("https://shakespeareframework.org/latest/manual");
      return webDriver.findElement(By.id("revnumber")).getText();
    }

    @Override
    public String toString() {
      return "latest shakespeare release version";
    }
  }
  // tag::example-question[]
}
