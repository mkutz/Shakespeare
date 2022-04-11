package org.shakespeareframework.selenium;

import static java.util.Arrays.stream;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariOptions;

/** {@link Enum} of all browser types which are supported by {@link WebDriverSupplier}. */
public enum BrowserType {

  /** Google Chrome */
  CHROME(org.openqa.selenium.chrome.ChromeDriver.class, new ChromeOptions()),

  /** Mozilla Firefox */
  FIREFOX(org.openqa.selenium.firefox.FirefoxDriver.class, new FirefoxOptions()),

  /** Microsoft Edge */
  EDGE(org.openqa.selenium.edge.EdgeDriver.class, new EdgeOptions()),

  /** Microsoft Internet Explorer */
  IEXPLORER(org.openqa.selenium.ie.InternetExplorerDriver.class, new InternetExplorerOptions()),

  /** Apple Safari */
  SAFARI(org.openqa.selenium.safari.SafariDriver.class, new SafariOptions());

  private final Class<? extends WebDriver> webDriverClass;

  private final ImmutableCapabilities baseCapabilities;

  BrowserType(Class<? extends WebDriver> webDriverClass, Capabilities baseCapabilities) {
    this.webDriverClass = webDriverClass;
    this.baseCapabilities = ImmutableCapabilities.copyOf(baseCapabilities);
  }

  /**
   * Looks up the named {@link BrowserType}, ignoring case.
   *
   * @param string the desired browser type as {@link String}
   * @return the desired {@link BrowserType} if known
   * @throws IllegalArgumentException if the given string does not equal any {@link
   *     BrowserType#name()}
   */
  public static BrowserType forName(String string) {
    return stream(values())
        .filter(browserType -> browserType.name().equalsIgnoreCase(string))
        .findAny()
        .orElseThrow(() -> new UnsupportedBrowserTypeException(string));
  }

  /**
   * Returns the {@link WebDriver} {@link Class} required to automate the {@link BrowserType}.
   *
   * @return the {@link WebDriver} {@link Class} required to automate the {@link BrowserType}
   */
  public Class<? extends WebDriver> getWebDriverClass() {
    return webDriverClass;
  }

  /**
   * Returns the base {@link Capabilities} of the {@link BrowserType}.
   *
   * @return the base {@link Capabilities} of the {@link BrowserType}
   */
  public Capabilities getBaseCapabilities() {
    return baseCapabilities;
  }
}
