package examples.kotlin

import io.github.bonigarcia.wdm.WebDriverManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeOptions
import org.shakespeareframework.Actor
import org.shakespeareframework.Question
import org.shakespeareframework.selenium.BrowseTheWeb
import org.shakespeareframework.selenium.BrowserType
import org.shakespeareframework.selenium.HtmlSnapshotReporter
import org.shakespeareframework.selenium.LocalWebDriverSupplier
import org.shakespeareframework.selenium.ScreenshotReporter
import org.shakespeareframework.selenium.WebDriverManagerWebDriverSupplier
import java.io.IOException
import java.nio.file.Path

@Execution(ExecutionMode.SAME_THREAD)
class SeleniumDocTest {

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun act1() {
    assumeThat(System.getenv("CI")).isNull()
    assumeThat(ProcessBuilder("which", "google-chrome").start().waitFor()).isEqualTo(0)

    // tag::local[]
    val tim = Actor("Tim").can(BrowseTheWeb(LocalWebDriverSupplier(BrowserType.CHROME)))
    // end::local[]

    val latestRelease = tim.checks(LatestShakespeareReleaseVersion())

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?")
  }

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun act2() {
    assumeThat(ProcessBuilder("which", "google-chrome").start().waitFor()).isEqualTo(0)

    // tag::additional-capabilities[]
    val cameron = Actor("Cameron")
      .can(
        BrowseTheWeb(
          LocalWebDriverSupplier(
            BrowserType.CHROME, ChromeOptions().addArguments("--headless")
          )
        )
      )
    // end::additional-capabilities[]

    val latestRelease = cameron.checks(LatestShakespeareReleaseVersion())

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?")
  }

  @Test
  @Disabled("fails with SessionNotCreatedException for no obvious reason")
  @Throws(IOException::class, InterruptedException::class)
  fun act3() {
    assumeThat(ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0)

    // tag::web-driver-manager[]
    val webDriverManager = WebDriverManager.edgedriver().browserInDocker()
    val alex = Actor("Alex")
      .can(
        BrowseTheWeb(
          WebDriverManagerWebDriverSupplier(webDriverManager, BrowserType.CHROME)
        )
      )
    // end::web-driver-manager[]

    val latestRelease = alex.checks(LatestShakespeareReleaseVersion())

    assertThat(latestRelease).matches("Version \\d+\\.\\d+(\\.\\d+)?")
  }

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun act4() {
    assumeThat(ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0)
    WebDriverManagerWebDriverSupplier(
      WebDriverManager.chromedriver().browserInDocker(), BrowserType.CHROME
    ).use { webDriverSupplier ->
      // tag::open-base-url[]
      val browseTheWeb = BrowseTheWeb(webDriverSupplier, "https://shakespeareframework.org/")

      assertThat(browseTheWeb.webDriver.currentUrl)
        .isEqualTo("https://shakespeareframework.org/")
      // end::open-base-url[]
    }
  }

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun act5() {
    assumeThat(ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0)

    // tag::screenshot-reporter[]
    val reportsPath = Path.of("build", "reports", "shakespeare")
    val imogen = Actor("Imogen")
      .can(
        BrowseTheWeb(
          WebDriverManagerWebDriverSupplier(
            WebDriverManager.firefoxdriver().browserInDocker(), BrowserType.FIREFOX
          )
        )
      )
      .informs(ScreenshotReporter(reportsPath, true))

    imogen.checks(LatestShakespeareReleaseVersion())

    assertThat(reportsPath.resolve("001-imogen-success-latest_shakespeare_release_version.png"))
      .isNotEmptyFile
    // end::screenshot-reporter[]
  }

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun act6() {
    assumeThat(ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0)

    // tag::html-snapshot-reporter[]
    val reportsPath = Path.of("build", "reports", "shakespeare")
    val tim = Actor("Tim")
      .can(
        BrowseTheWeb(
          WebDriverManagerWebDriverSupplier(
            WebDriverManager.firefoxdriver().browserInDocker(), BrowserType.FIREFOX
          )
        )
      )
      .informs(HtmlSnapshotReporter(reportsPath, true))

    tim.checks(LatestShakespeareReleaseVersion())

    assertThat(reportsPath.resolve("001-tim-success-latest_shakespeare_release_version.html"))
      .isNotEmptyFile
    // end::html-snapshot-reporter[]
  }

  // tag::example-question[]
  class LatestShakespeareReleaseVersion : Question<String> {

    override fun answerAs(actor: Actor): String {
      val webDriver = actor.uses(BrowseTheWeb::class.java).webDriver
      webDriver.get("https://shakespeareframework.org/latest/manual")
      return webDriver.findElement(By.id("revnumber")).text
    }

    override fun toString(): String {
      return "latest shakespeare release version"
    }
  }
  // tag::example-question[]
}
