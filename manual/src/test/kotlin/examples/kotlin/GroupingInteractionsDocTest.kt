package examples.kotlin

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeOptions
import org.shakespeareframework.Actor
import org.shakespeareframework.Question
import org.shakespeareframework.Task
import org.shakespeareframework.selenium.BrowseTheWeb
import org.shakespeareframework.selenium.LocalWebDriverSupplier
import java.io.IOException

class GroupingInteractionsDocTest {

  companion object {
    const val LOGIN_FORM_HTML = """
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
        """

    const val LOGGED_IN_HTML = """
            <html>
                <body>
                    <a href="/logout">Log Out</a>
                </body>
            </html>
        """

    val mockWebServer = MockWebServer()

    @JvmStatic
    @BeforeAll
    @Throws(IOException::class)
    fun initializeMockWebServer() {
      mockWebServer.enqueue(MockResponse().addHeader("Content-Type", "text/html").setBody(LOGIN_FORM_HTML))
      mockWebServer.enqueue(MockResponse().setBody(LOGGED_IN_HTML))
      mockWebServer.start()
    }

    @JvmStatic
    @AfterAll
    @Throws(IOException::class)
    fun shutdownMockWebServer() {
      mockWebServer.shutdown()
    }
  }

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun act1() {
    assumeThat(ProcessBuilder("which", "google-chrome").start().waitFor()).isEqualTo(0)
    val browseTheWeb = BrowseTheWeb(LocalWebDriverSupplier(BrowserType.CHROME, ChromeOptions().addArguments("--headless")))
    // tag::create-actor[]
    val john = Actor("John").can(browseTheWeb)
    // end::create-actor[]
    // tag::do-task[]
    john.does(Login("john", "demo"))
    // end::do-task[]
    // tag::check-question[]
    assertThat(john.checks(LoggedInState())).isTrue()
    // end::check-question[]
  }

  // tag::task[]
  data class Login(private val username: String, private val password: String) : Task {

    override fun performAs(actor: Actor) {
      val webDriver = actor.uses(BrowseTheWeb::class.java).webDriver

      webDriver.get(mockWebServer.url("/").toString()) // <1>

      webDriver
        .findElement(By.name("username")) // <2>
        .sendKeys(username) // <3>
      webDriver
        .findElement(By.name("password")) // <4>
        .sendKeys(password) // <5>
      webDriver
        .findElement(By.cssSelector(".login button")) // <6>
        .click() // <7>
    }

    override fun toString(): String {
      return "login as $username"
    }
  }
  // end::task[]

  // tag::question[]
  class LoggedInState : Question<Boolean> {

    override fun answerAs(actor: Actor): Boolean {
      val webDriver = actor.uses(BrowseTheWeb::class.java).webDriver
      return webDriver.findElements(By.linkText("Log Out")).isNotEmpty()
    }

    override fun toString(): String {
      return "login state"
    }
  }
  // end::question[]
}
