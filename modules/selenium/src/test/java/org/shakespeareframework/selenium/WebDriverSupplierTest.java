package org.shakespeareframework.selenium;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class WebDriverSupplierTest {

    @ParameterizedTest(name = "getCapabilities merges additional and base capabilities of {0}")
    @EnumSource(BrowserType.class)
    void getCapabilitiesTest1(BrowserType browserType) {
        final var additionalCapabilities = new MutableCapabilities(Map.of("additionalCapability", "true"));

        final var mockWebDriverSupplier = new MockWebDriverSupplier(browserType, additionalCapabilities);

        assertThat(mockWebDriverSupplier.getCapabilities().getCapability("additionalCapability"))
                .isEqualTo("true");
        assertThat(mockWebDriverSupplier.getCapabilities().asMap())
                .containsAllEntriesOf(browserType.getBaseCapabilities().asMap());
    }

    @ParameterizedTest(name = "toString returns a loggable string")
    @EnumSource(BrowserType.class)
    void toStringTest1(BrowserType browserType) {
        assertThat(new MockWebDriverSupplier(browserType, null))
                .hasToString(browserType.name());
    }

    private static class MockWebDriverSupplier extends WebDriverSupplier {

        public MockWebDriverSupplier(BrowserType browserType, Capabilities additionalCapabilities) {
            super(browserType, additionalCapabilities);
        }

        @Override
        public void close() {
        }

        @Override
        public WebDriver get() {
            return mock(WebDriver.class);
        }
    }
}