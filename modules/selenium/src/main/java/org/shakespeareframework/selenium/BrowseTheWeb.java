package org.shakespeareframework.selenium;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import org.openqa.selenium.WebDriver;
import org.shakespeareframework.Ability;

/** {@link Ability} to browse the web using a Selenium {@link WebDriver}. */
public final class BrowseTheWeb implements Ability, AutoCloseable {

  private final WebDriverSupplier webDriverSupplier;
  private final String baseUrl;

  /**
   * @param webDriverSupplier the {@link WebDriverSupplier} used to setup the {@link WebDriver}
   * @param baseUrl a URL to be opened automatically
   */
  public BrowseTheWeb(WebDriverSupplier webDriverSupplier, String baseUrl) {
    this.webDriverSupplier = requireNonNull(webDriverSupplier);
    this.baseUrl = baseUrl;
  }

  /**
   * @param webDriverSupplier the {@link WebDriverSupplier} used to setup the {@link WebDriver}
   */
  public BrowseTheWeb(WebDriverSupplier webDriverSupplier) {
    this(webDriverSupplier, null);
  }

  /**
   * Gets the {@link WebDriver} from the {@link #webDriverSupplier} and returns it.
   *
   * <p>If a {@link #baseUrl} is given, it will be opened automatically.
   *
   * @return a {@link WebDriver} instance.
   * @see WebDriverSupplier#get()
   */
  public WebDriver getWebDriver() {
    final var webDriver = webDriverSupplier.get();
    if (baseUrl != null && !webDriver.getCurrentUrl().startsWith("http")) {
      webDriver.get(baseUrl);
    }
    return webDriver;
  }

  @Override
  public void close() throws Exception {
    webDriverSupplier.close();
  }

  @Override
  public String toString() {
    return format("browse the web using %s", webDriverSupplier);
  }
}
