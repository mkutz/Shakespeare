package org.shakespeareframework.selenium;

import java.util.function.Supplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * A {@link Supplier} for {@link WebDriver}, which also takes care of {@link WebDriver#quit()
 * quitting} the {@link WebDriver}.
 */
public abstract class WebDriverSupplier implements Supplier<WebDriver>, AutoCloseable {

  private final BrowserType browserType;
  private final Capabilities capabilities;

  /**
   * Creates a {@link WebDriverSupplier} for the given {@link BrowserType} with additional {@link
   * Capabilities} that will be {@link Capabilities#merge(Capabilities) merged} with the {@link
   * BrowserType#getBaseCapabilities BrowserType's baseCapabilities}.
   *
   * @param browserType the {@link BrowserType} to be setup
   * @param additionalCapabilities additional {@link Capabilities} for the {@link WebDriver}
   */
  protected WebDriverSupplier(BrowserType browserType, Capabilities additionalCapabilities) {
    this.browserType = browserType;
    this.capabilities =
        additionalCapabilities == null
            ? browserType.getBaseCapabilities()
            : browserType.getBaseCapabilities().merge(additionalCapabilities);
  }

  /**
   * Creates a {@link WebDriverSupplier} for the given {@link BrowserType} using the {@link
   * BrowserType#getBaseCapabilities BrowserType's baseCapabilities}.
   *
   * @return the {@link BrowserType}
   */
  public BrowserType getBrowserType() {
    return browserType;
  }

  /**
   * Returns the {@link Capabilities} for the {@link WebDriver}, merged from the {@link
   * BrowserType#getBaseCapabilities BrowserType's baseCapabilities} and additional capabilities.
   *
   * @return the {@link Capabilities} that will be used for the supplied {@link WebDriver}
   */
  public Capabilities getCapabilities() {
    return capabilities;
  }

  @Override
  public String toString() {
    return browserType.name();
  }
}
