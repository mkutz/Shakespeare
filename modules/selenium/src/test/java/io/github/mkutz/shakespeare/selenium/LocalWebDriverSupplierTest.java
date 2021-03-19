package io.github.mkutz.shakespeare.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;

import static io.github.mkutz.shakespeare.selenium.BrowserType.CHROME;
import static io.github.mkutz.shakespeare.selenium.BrowserType.FIREFOX;
import static io.github.mkutz.shakespeare.selenium.BrowserType.SAFARI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class LocalWebDriverSupplierTest {

    @Test
    @DisplayName("get returns a ChromeDriver")
    void getTest1() throws IOException, InterruptedException {
        assumeTrue(new ProcessBuilder("which", "google-chrome").start().waitFor() == 0);

        final var localWebDriverSupplier = new LocalWebDriverSupplier(CHROME);

        assertThat(localWebDriverSupplier.get())
                .isNotNull()
                .isExactlyInstanceOf(ChromeDriver.class);

        localWebDriverSupplier.close();
    }

    @Test
    @DisplayName("get returns a FirefoxDriver")
    void getTest2() throws IOException, InterruptedException {
        assumeTrue(new ProcessBuilder("which", "firefox").start().waitFor() == 0);

        final var localWebDriverSupplier = new LocalWebDriverSupplier(FIREFOX);

        assertThat(localWebDriverSupplier.get())
                .isNotNull()
                .isExactlyInstanceOf(FirefoxDriver.class);

        localWebDriverSupplier.close();
    }

    @Test
    @DisplayName("get returns a SafariDriver")
    void getTest3() throws IOException, InterruptedException {
        assumeTrue(new ProcessBuilder("which", "safari").start().waitFor() == 0);

        final var localWebDriverSupplier = new LocalWebDriverSupplier(SAFARI);

        assertThat(localWebDriverSupplier.get())
                .isNotNull()
                .isExactlyInstanceOf(SafariDriver.class);

        localWebDriverSupplier.close();
    }
}