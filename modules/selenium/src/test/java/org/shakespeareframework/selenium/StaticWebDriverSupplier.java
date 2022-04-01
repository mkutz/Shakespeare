package org.shakespeareframework.selenium;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

class StaticWebDriverSupplier extends WebDriverSupplier {

  private final WebDriver webDriver;
  private boolean closed = false;

  public StaticWebDriverSupplier(
      WebDriver webDriver, BrowserType browserType, Capabilities additionalCapabilities) {
    super(browserType, additionalCapabilities);
    this.webDriver = webDriver;
  }

  @Override
  public void close() {
    this.closed = true;
  }

  @Override
  public WebDriver get() {
    return webDriver;
  }

  public boolean isClosed() {
    return closed;
  }
}
