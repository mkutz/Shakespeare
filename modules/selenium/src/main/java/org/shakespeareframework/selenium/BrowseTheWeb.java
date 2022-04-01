package org.shakespeareframework.selenium;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import org.openqa.selenium.WebDriver;
import org.shakespeareframework.Ability;

/** {@link Ability} to browse the web using a Selenium {@link WebDriver}. */
public final class BrowseTheWeb implements Ability, AutoCloseable {

  private final WebDriverSupplier webDriverSupplier;

  /** @param webDriverSupplier the {@link WebDriverSupplier} used to setup the {@link WebDriver} */
  public BrowseTheWeb(WebDriverSupplier webDriverSupplier) {
    this.webDriverSupplier = requireNonNull(webDriverSupplier);
  }

  /**
   * Gets the {@link WebDriver} from the {@link #webDriverSupplier} and returns it.
   *
   * @return a {@link WebDriver} instance
   * @see WebDriverSupplier#get()
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
    return format("browse the web using %s", webDriverSupplier);
  }
}
