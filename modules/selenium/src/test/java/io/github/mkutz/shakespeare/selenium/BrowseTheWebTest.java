package io.github.mkutz.shakespeare.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BrowseTheWebTest {

    @Test
    @DisplayName("getWebDriver uses the WebDriverSupplier")
    void getWebDriverTest1() {
        final var webDriverSupplierMock = mock(WebDriverSupplier.class);
        final var browserTheWeb = new BrowseTheWeb(webDriverSupplierMock);

        browserTheWeb.getWebDriver();

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
        assertThat(new BrowseTheWeb(webDriverSupplierMock).toString())
                .isEqualTo("browse the web using %s".formatted(webDriverSupplierMock.toString()));
    }
}