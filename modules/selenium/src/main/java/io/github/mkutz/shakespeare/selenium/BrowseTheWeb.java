package io.github.mkutz.shakespeare.selenium;

import io.github.mkutz.shakespeare.Ability;
import org.openqa.selenium.WebDriver;

import static java.util.Objects.requireNonNull;

/**
 * {@link io.github.mkutz.shakespeare.Ability} to browse the web using a Selenium {@link org.openqa.selenium.WebDriver}.
 */
public record BrowseTheWeb(WebDriverSupplier webDriverSupplier) implements Ability, AutoCloseable {

    /**
     * @param webDriverSupplier the {@link io.github.mkutz.shakespeare.selenium.WebDriverSupplier} used to setup the {@link org.openqa.selenium.WebDriver}
     */
    public BrowseTheWeb {
        requireNonNull(webDriverSupplier);
    }

    /**
     * @return a {@link org.openqa.selenium.WebDriver} instance
     */
    public WebDriver getWebDriver() {
        return webDriverSupplier.get();
    }

    @Override
    public void close() throws Exception {
        webDriverSupplier.close();
    }

    @Override
    public String toString() {
        return "browse the web using %s".formatted(webDriverSupplier);
    }
}
