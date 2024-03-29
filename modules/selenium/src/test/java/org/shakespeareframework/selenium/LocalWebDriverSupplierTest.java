package org.shakespeareframework.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.shakespeareframework.selenium.BrowserType.CHROME;
import static org.shakespeareframework.selenium.BrowserType.FIREFOX;

import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

class LocalWebDriverSupplierTest {

  private static final Map<BrowserType, String> COMMANDS =
      Map.of(CHROME, "google-chrome", FIREFOX, "firefox");

  private static final Map<BrowserType, Capabilities> HEADLESS =
      Map.of(
          CHROME, new ChromeOptions().addArguments("--headless"),
          FIREFOX, new FirefoxOptions().addArguments("-headless"));

  @ParameterizedTest(name = "get returns a {0}")
  @EnumSource(BrowserType.class)
  void getTest1(BrowserType browserType) throws IOException, InterruptedException {
    assumeThat(COMMANDS).containsKey(browserType);
    assumeThat(new ProcessBuilder("which", COMMANDS.get(browserType)).start().waitFor())
        .isEqualTo(0);

    var localWebDriverSupplier = new LocalWebDriverSupplier(browserType, HEADLESS.get(browserType));

    assertThat(localWebDriverSupplier.get())
        .isNotNull()
        .isExactlyInstanceOf(browserType.getWebDriverClass());

    localWebDriverSupplier.close();
  }

  @Test
  @DisplayName("get caches the WebDriver")
  void getTest2() throws IOException, InterruptedException {
    assumeThat(COMMANDS).containsKey(FIREFOX);
    assumeThat(new ProcessBuilder("which", COMMANDS.get(FIREFOX)).start().waitFor()).isEqualTo(0);

    var localWebDriverSupplier = new LocalWebDriverSupplier(FIREFOX, HEADLESS.get(FIREFOX));

    var webDriver = localWebDriverSupplier.get();
    assertThat(webDriver).isSameAs(localWebDriverSupplier.get());

    localWebDriverSupplier.close();
  }

  @Test
  @DisplayName("close quits the WebDriver")
  void closeTest1() throws IOException, InterruptedException {
    assumeThat(COMMANDS).containsKey(FIREFOX);
    assumeThat(new ProcessBuilder("which", COMMANDS.get(FIREFOX)).start().waitFor()).isEqualTo(0);

    var localWebDriverSupplier = new LocalWebDriverSupplier(FIREFOX, HEADLESS.get(FIREFOX));

    var webDriver = localWebDriverSupplier.get();
    var window = webDriver.manage().window();

    assertThatNoException().isThrownBy(window::getSize);

    localWebDriverSupplier.close();

    assertThatExceptionOfType(NoSuchSessionException.class).isThrownBy(window::getSize);
  }
}
