package io.github.mkutz.shakespeare.selenium;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.EnumMap;
import java.util.Map;

/**
 * {@link WebDriverSupplier} relying on <a href="https://www.testcontainers.org/">Testcontainers</a>'
 * {@link BrowserWebDriverContainer} to run browsers on a local Docker infrastructure.
 */
public class DockerWebDriverSupplier extends WebDriverSupplier {

    /**
     * {@link Map} of running {@link BrowserWebDriverContainer}s mapped to their {@link BrowserType}. Generally it is
     * possible to run multiple containers in parallel however, as each container will have a significant need for
     * memory and CPU time, it should ideally be limited to one only!
     */
    private static final Map<BrowserType, BrowserWebDriverContainer<?>> webDriverContainers =
            new EnumMap<>(BrowserType.class);

    private WebDriver webDriver;

    /**
     * @param browserType            the {@link BrowserType} to be setup
     * @param additionalCapabilities additional {@link Capabilities} for the {@link WebDriver}
     */
    public DockerWebDriverSupplier(BrowserType browserType, Capabilities additionalCapabilities) {
        super(browserType, additionalCapabilities);
    }

    /**
     * @param browserType the {@link BrowserType} to be setup
     */
    public DockerWebDriverSupplier(BrowserType browserType) {
        this(browserType, null);
    }

    @Override
    public WebDriver get() {
        final var browserType = getBrowserType();
        final var capabilities = getCapabilities();
        var webDriverContainer = webDriverContainers.get(browserType);
        if (webDriverContainer == null || !webDriverContainer.isRunning()) {
            webDriverContainer = new BrowserWebDriverContainer<>().withCapabilities(capabilities);
            webDriverContainer.start();
            webDriverContainers.put(browserType, webDriverContainer);
        }
        if (webDriver == null) {
            webDriver = new RemoteWebDriver(
                    webDriverContainer.getSeleniumAddress(),
                    capabilities);
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
