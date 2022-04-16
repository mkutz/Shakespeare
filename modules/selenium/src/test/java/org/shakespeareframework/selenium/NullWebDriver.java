package org.shakespeareframework.selenium;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

class NullWebDriver implements WebDriver, TakesScreenshot {

  private boolean closed = false;
  private boolean quit = false;
  private String currentUrl = "";

  @Override
  public void get(String url) {
    currentUrl = url;
  }

  @Override
  public String getCurrentUrl() {
    return currentUrl;
  }

  @Override
  public String getTitle() {
    return "Page";
  }

  @Override
  public List<WebElement> findElements(By by) {
    return List.of();
  }

  @Override
  public WebElement findElement(By by) {
    return null;
  }

  @Override
  public String getPageSource() {
    return "<html/>";
  }

  @Override
  public void close() {
    this.closed = true;
  }

  public boolean isClosed() {
    return closed;
  }

  @Override
  public void quit() {
    this.quit = true;
  }

  public boolean isQuit() {
    return quit;
  }

  @Override
  public Set<String> getWindowHandles() {
    return Set.of();
  }

  @Override
  public String getWindowHandle() {
    return "window-handle";
  }

  @Override
  public TargetLocator switchTo() {
    return null;
  }

  @Override
  public Navigation navigate() {
    return null;
  }

  @Override
  public Options manage() {
    return null;
  }

  @Override
  public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
    return target.convertFromPngBytes("png".getBytes(StandardCharsets.UTF_8));
  }
}
