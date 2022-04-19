package org.shakespeareframework.selenium;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BrowseTheWebTest {

  @Test
  @DisplayName("getWebDriver uses the WebDriverSupplier")
  void getWebDriverTest1() {
    var nullWebDriver = new NullWebDriver();
    var webDriverSupplier = new StaticWebDriverSupplier(nullWebDriver, BrowserType.CHROME, null);
    var browserTheWeb = new BrowseTheWeb(webDriverSupplier);

    assertThat(browserTheWeb.getWebDriver()).isEqualTo(nullWebDriver);
  }

  @Test
  @DisplayName("close closes the WebDriverSupplier")
  void closeTest1() throws Exception {
    var webDriverSupplier =
        new StaticWebDriverSupplier(new NullWebDriver(), BrowserType.CHROME, null);
    var browserTheWeb = new BrowseTheWeb(webDriverSupplier);

    browserTheWeb.close();

    assertThat(webDriverSupplier.isClosed()).isTrue();
  }

  @Test
  @DisplayName("toString returns a loggable string")
  void toStringTest() {
    var webDriverSupplierMock =
        new StaticWebDriverSupplier(new NullWebDriver(), BrowserType.CHROME, null);
    assertThat(new BrowseTheWeb(webDriverSupplierMock))
        .hasToString(format("browse the web using %s", webDriverSupplierMock));
  }

  @Test
  @DisplayName("getWebDriver opens the base URL automatically")
  void getWebDriverTest2() {
    var nullWebDriver = new NullWebDriver();
    var webDriverSupplier = new StaticWebDriverSupplier(nullWebDriver, BrowserType.CHROME, null);
    var baseUrl = "https://shakespeareframework.org/somewhere/else";
    var browserTheWeb = new BrowseTheWeb(webDriverSupplier, baseUrl);

    assertThat(browserTheWeb.getWebDriver().getCurrentUrl()).isEqualTo(baseUrl);
  }

  @Test
  @DisplayName("getWebDriver ignores the base URL if already navigated")
  void getWebDriverTest3() {
    var nullWebDriver = new NullWebDriver();
    var webDriverSupplier = new StaticWebDriverSupplier(nullWebDriver, BrowserType.CHROME, null);
    var baseUrl = "https://shakespeareframework.org/somewhere/else";
    var currentUrl = "https://shakespeareframework.org/previous/navigation";
    var browserTheWeb = new BrowseTheWeb(webDriverSupplier, baseUrl);

    nullWebDriver.get(currentUrl);
    assertThat(browserTheWeb.getWebDriver().getCurrentUrl()).isEqualTo(currentUrl);
  }
}
