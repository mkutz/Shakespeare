package org.shakespeareframework.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * A {@link WebDriverSupplier} for Docker based browser instances. Uses {@link WebDriverManager} to manage the container
 * and create the {@link WebDriver}.
 */
public class DockerWebDriverSupplier extends WebDriverManagerWebDriverSupplier {

    /**
     * @param browserType            the {@link BrowserType} to be setup
     * @param additionalCapabilities additional {@link Capabilities} for the {@link WebDriver}
     */
    public DockerWebDriverSupplier(BrowserType browserType, Capabilities additionalCapabilities) {
        super(WebDriverManager.getInstance(browserType.getWebDriverClass()).browserInDocker(),
                browserType, additionalCapabilities);
    }

    /**
     * @param browserType the {@link BrowserType} to be setup
     */
    public DockerWebDriverSupplier(BrowserType browserType) {
        this(browserType, null);
    }
}
