package org.shakespeareframework.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class BrowseTheWebTest {

    @Test
    @DisplayName("getWebDriver uses the WebDriverSupplier")
    void getWebDriverTest1() {
        final var nullWebDriver = new NullWebDriver();
        final var webDriverSupplier = new StaticWebDriverSupplier(nullWebDriver, BrowserType.CHROME, null);
        final var browserTheWeb = new BrowseTheWeb(webDriverSupplier);

        assertThat(browserTheWeb.getWebDriver()).isEqualTo(nullWebDriver);
    }

    @Test
    @DisplayName("close closes the WebDriverSupplier")
    void closeTest1() throws Exception {
        final var webDriverSupplier = new StaticWebDriverSupplier(new NullWebDriver(), BrowserType.CHROME, null);
        final var browserTheWeb = new BrowseTheWeb(webDriverSupplier);

        browserTheWeb.close();

        assertThat(webDriverSupplier.isClosed()).isTrue();
    }

    @Test
    @DisplayName("toString returns a loggable string")
    void toStringTest() {
        final var webDriverSupplierMock = new StaticWebDriverSupplier(new NullWebDriver(), BrowserType.CHROME, null);
        assertThat(new BrowseTheWeb(webDriverSupplierMock))
                .hasToString(format("browse the web using %s", webDriverSupplierMock));
    }
}
