package org.shakespeareframework.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * A {@link WebDriverSupplier} based on {@link WebDriverManager}.
 */
public class WebDriverManagerWebDriverSupplier extends WebDriverSupplier {

    private final WebDriverManager webDriverManager;
    private WebDriver webDriver;

    public WebDriverManagerWebDriverSupplier(WebDriverManager webDriverManager,
            BrowserType browserType, Capabilities additionalCapabilities) {
        super(browserType, additionalCapabilities);
        this.webDriverManager = webDriverManager;
    }

    /**
     * @param browserType the {@link BrowserType} to be setup
     */
    public WebDriverManagerWebDriverSupplier(BrowserType browserType, WebDriverManager webDriverManager) {
        this(webDriverManager, browserType, null);
    }

    @Override
    public WebDriver get() {
        if (webDriver == null) {
            webDriver = webDriverManager
                    .capabilities(getCapabilities())
                    .create();
        }
        return webDriver;
    }

    @Override
    public void close() {
        webDriverManager.quit();
        webDriver = null;
    }
}
