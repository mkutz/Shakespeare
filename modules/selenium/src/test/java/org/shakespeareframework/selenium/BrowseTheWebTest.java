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
    var webDriverSupplier =
        new StaticWebDriverSupplier(nullWebDriver, BrowserType.CHROME, null);
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
}
