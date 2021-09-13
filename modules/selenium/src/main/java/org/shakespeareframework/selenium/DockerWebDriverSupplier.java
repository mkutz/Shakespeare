package org.shakespeareframework.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * A {@link WebDriverSupplier} for Docker based browser instances. Uses {@link WebDriverManager} to manage the container
 * and create the {@link WebDriver}.
 */
public class DockerWebDriverSupplier extends WebDriverSupplier {

    private final WebDriverManager webDriverManager;
    private WebDriver webDriver;

    /**
     * @param browserType            the {@link BrowserType} to be setup
     * @param additionalCapabilities additional {@link Capabilities} for the {@link WebDriver}
     */
    public DockerWebDriverSupplier(BrowserType browserType, Capabilities additionalCapabilities) {
        super(browserType, additionalCapabilities);
        this.webDriverManager = WebDriverManager
                .getInstance(browserType.getWebDriverClass())
                .capabilities(getCapabilities())
                .browserInDocker();
    }

    /**
     * @param browserType the {@link BrowserType} to be setup
     */
    public DockerWebDriverSupplier(BrowserType browserType) {
        this(browserType, null);
    }

    @Override
    public WebDriver get() {
        if (webDriver == null) {
            webDriver = webDriverManager.create();
        }
        return webDriver;
    }

    @Override
    public void close() {
        webDriverManager.quit();
        webDriver = null;
    }
}
