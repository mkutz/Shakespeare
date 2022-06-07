package org.shakespeareframework.playwrite;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import org.shakespeareframework.Ability;

public class BrowseTheWeb implements Ability, AutoCloseable {

  private static final Playwright playwright = Playwright.create();

  private Browser browser;

  Browser getBrowser() {
    if (browser == null) {
      this.browser = playwright.firefox().launch();
    }
    return browser;
  }

  @Override
  public void close() {
    playwright.close();
  }
}
