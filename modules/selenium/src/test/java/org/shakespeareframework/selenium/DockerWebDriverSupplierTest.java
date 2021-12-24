package org.shakespeareframework.selenium;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

class DockerWebDriverSupplierTest {

    @ParameterizedTest(name = "get works for {0}")
    @EnumSource(value = BrowserType.class, names = {"CHROME", "FIREFOX"})
    void getTest1(BrowserType browserType) throws IOException, InterruptedException {
        assumeThat(new ProcessBuilder("which", "docker").start().waitFor()).isEqualTo(0);

        final var dockerWebDriverSupplier = new DockerWebDriverSupplier(browserType);

        assertThat(dockerWebDriverSupplier.get())
                .isNotNull()
                .isExactlyInstanceOf(RemoteWebDriver.class);

        dockerWebDriverSupplier.close();
    }
}