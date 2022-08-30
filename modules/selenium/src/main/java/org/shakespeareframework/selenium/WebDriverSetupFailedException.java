package org.shakespeareframework.selenium;

/**
 * {@link RuntimeException} to be thrown when a {@link WebDriverSupplier} failed to setup the {@link
 * org.openqa.selenium.WebDriver}.
 */
public class WebDriverSetupFailedException extends RuntimeException {

  /**
   * @param cause the causing {@link Exception}
   */
  public WebDriverSetupFailedException(Exception cause) {
    super(cause);
  }
}
