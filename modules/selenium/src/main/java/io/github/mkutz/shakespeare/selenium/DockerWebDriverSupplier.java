package io.github.mkutz.shakespeare.selenium;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

/**
 * {@link WebDriverSupplier} relying on <a href="https://www.testcontainers.org/">Testcontainers</a>'
 * {@link BrowserWebDriverContainer} to run browsers on a local Docker infrastructure.
 */
public class DockerWebDriverSupplier extends WebDriverSupplier {

    private BrowserWebDriverContainer<?> webDriverContainer;
    private WebDriver webDriver;

    /**
     * @param browserType            the {@link BrowserType} to be setup
     * @param additionalCapabilities additional {@link Capabilities} that will be merged with the
     *                               {@link BrowserType#getBaseCapabilities() baseCapabilities}
     */
    public DockerWebDriverSupplier(BrowserType browserType, Capabilities additionalCapabilities) {
        super(browserType, additionalCapabilities);
    }

    /**
     * @param browserType the {@link BrowserType} to be setup with no additional capabilities
     */
    public DockerWebDriverSupplier(BrowserType browserType) {
        this(browserType, null);
    }

    @Override
    public WebDriver get() {
        if (webDriverContainer == null || !webDriverContainer.isRunning()) {
            webDriverContainer = new BrowserWebDriverContainer<>()
                    .withCapabilities(getCapabilities());
            webDriverContainer.start();
        }
        if (webDriver == null) {
            webDriver = new RemoteWebDriver(
                    webDriverContainer.getSeleniumAddress(),
                    getCapabilities());
        }
        return webDriver;
    }

    @Override
    public void close() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }
}
