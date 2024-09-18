package org.shakespeareframework.selenium;

import static java.util.Objects.requireNonNull;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/** A {@link WebDriverSupplier} based on {@link WebDriverManager}. */
@NullMarked
public class WebDriverManagerWebDriverSupplier extends WebDriverSupplier {

  private final WebDriverManager webDriverManager;
  @Nullable private WebDriver webDriver;

  /**
   * @param webDriverManager the {@link WebDriverManager} that will be used to create the WebDriver
   * @param browserType the {@link BrowserType} to be setup
   * @param additionalCapabilities additional {@link Capabilities} for the {@link WebDriver}
   */
  public WebDriverManagerWebDriverSupplier(
      WebDriverManager webDriverManager,
      BrowserType browserType,
      @Nullable Capabilities additionalCapabilities) {
    super(browserType, additionalCapabilities);
    this.webDriverManager = webDriverManager;
  }

  /**
   * @param webDriverManager the {@link WebDriverManager} that will be used to create the WebDriver
   * @param browserType the {@link BrowserType} to be setup
   */
  public WebDriverManagerWebDriverSupplier(
      WebDriverManager webDriverManager, BrowserType browserType) {
    this(webDriverManager, browserType, null);
  }

  @Override
  public WebDriver get() {
    if (webDriver == null) {
      webDriver =
          requireNonNull(
              webDriverManager.capabilities(getCapabilities()).create(),
              "Creating the WebDriver failed");
    }
    return webDriver;
  }

  @Override
  public void close() {
    webDriverManager.quit();
    webDriver = null;
  }
}
