package org.shakespeareframework.selenium;

import static java.lang.String.format;

import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.WebDriver;
import org.shakespeareframework.Ability;

/** {@link Ability} to browse the web using a Selenium {@link WebDriver}. */
@NullMarked
public class BrowseTheWeb implements Ability {

  private final WebDriver webDriver;
  @Nullable private final String baseUrl;

  /**
   * @param webDriver the {@link WebDriver}
   * @param baseUrl a URL to be opened automatically
   */
  public BrowseTheWeb(WebDriver webDriver, @Nullable String baseUrl) {
    this.webDriver = webDriver;
    this.baseUrl = baseUrl;
  }

  /**
   * @param webDriver the {@link WebDriver}
   */
  public BrowseTheWeb(WebDriver webDriver) {
    this(webDriver, null);
  }

  /**
   * Returns the {@link #webDriver} instance. If a {@link #baseUrl} is given, it will be opened
   * automatically.
   *
   * @return a {@link WebDriver} instance
   */
  public WebDriver getWebDriver() {
    if (baseUrl != null
        && !Objects.requireNonNullElse(webDriver.getCurrentUrl(), "").startsWith("http")) {
      webDriver.get(baseUrl);
    }
    return webDriver;
  }

  @Override
  public String toString() {
    return format("browse the web using %s", webDriver);
  }
}
