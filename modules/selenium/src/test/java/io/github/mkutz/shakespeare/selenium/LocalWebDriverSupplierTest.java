package io.github.mkutz.shakespeare.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.util.Map;

import static io.github.mkutz.shakespeare.selenium.BrowserType.CHROME;
import static io.github.mkutz.shakespeare.selenium.BrowserType.FIREFOX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class LocalWebDriverSupplierTest {

    private static final Map<BrowserType, String> COMMANDS = Map.of(
            CHROME, "google-chrome",
            FIREFOX, "firefox");

    private static final Map<BrowserType, Capabilities> HEADLESS = Map.of(
            CHROME, new ChromeOptions().setHeadless(true),
            FIREFOX, new FirefoxOptions().setHeadless(true));

    @ParameterizedTest(name = "get returns a {0}")
    @EnumSource(BrowserType.class)
    void getTest1(BrowserType browserType) throws IOException, InterruptedException {
        assumeTrue(COMMANDS.containsKey(browserType));
        assumeTrue(new ProcessBuilder("which", COMMANDS.get(browserType)).start().waitFor() == 0);

        final var localWebDriverSupplier = new LocalWebDriverSupplier(browserType, HEADLESS.get(browserType));

        assertThat(localWebDriverSupplier.get())
                .isNotNull()
                .isExactlyInstanceOf(browserType.getWebDriverClass());

        localWebDriverSupplier.close();
    }

    @Test
    @DisplayName("get caches the WebDriver")
    void getTest2() {
        final var localWebDriverSupplier = new LocalWebDriverSupplier(FIREFOX);

        final var webDriver = localWebDriverSupplier.get();
        assertThat(webDriver).isSameAs(localWebDriverSupplier.get());

        localWebDriverSupplier.close();
    }

    @Test
    @DisplayName("close does nothing when no WebDriver was created")
    void closeTest1() {
        new LocalWebDriverSupplier(FIREFOX).close();
    }
}