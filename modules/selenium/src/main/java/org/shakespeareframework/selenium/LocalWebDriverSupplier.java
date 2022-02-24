package org.shakespeareframework.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * A {@link WebDriverSupplier} for locally installed browser instances. Uses {@link WebDriverManager} to set up the
 * local binary and create the {@link WebDriver}.
 */
public class LocalWebDriverSupplier extends WebDriverManagerWebDriverSupplier {

    /**
     * @param browserType            the {@link BrowserType} to be setup
     * @param additionalCapabilities additional {@link Capabilities} for the {@link WebDriver}
     */
    public LocalWebDriverSupplier(BrowserType browserType, Capabilities additionalCapabilities) {
        super(WebDriverManager.getInstance(browserType.getWebDriverClass()),
                browserType, additionalCapabilities);
    }

    /**
     * @param browserType the {@link BrowserType} to be setup
     */
    public LocalWebDriverSupplier(BrowserType browserType) {
        this(browserType, null);
    }
}
