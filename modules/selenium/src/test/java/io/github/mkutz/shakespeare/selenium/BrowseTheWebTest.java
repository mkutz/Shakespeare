package io.github.mkutz.shakespeare.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BrowseTheWebTest {

    @Test
    @DisplayName("getWebDriver uses the WebDriverSupplier")
    void getWebDriverTest1() {
        final var webDriverSupplierMock = mock(WebDriverSupplier.class);
        final var webDriverMock = mock(WebDriver.class);
        when(webDriverSupplierMock.get()).thenReturn(webDriverMock);
        final var browserTheWeb = new BrowseTheWeb(webDriverSupplierMock);

        assertThat(browserTheWeb.getWebDriver()).isEqualTo(webDriverMock);

        verify(webDriverSupplierMock, times(1)).get();
    }

    @Test
    @DisplayName("close closes the WebDriverSupplier")
    void closeTest1() throws Exception {
        final var webDriverSupplierMock = mock(WebDriverSupplier.class);
        final var browserTheWeb = new BrowseTheWeb(webDriverSupplierMock);

        browserTheWeb.close();

        verify(webDriverSupplierMock, times(1)).close();
    }

    @Test
    @DisplayName("toString returns a loggable string")
    void toStringTest() {
        final var webDriverSupplierMock = mock(WebDriverSupplier.class);
        assertThat(new BrowseTheWeb(webDriverSupplierMock))
                .hasToString("browse the web using %s".formatted(webDriverSupplierMock.toString()));
    }
}