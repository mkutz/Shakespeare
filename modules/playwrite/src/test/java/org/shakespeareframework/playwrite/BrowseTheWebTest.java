package org.shakespeareframework.playwrite;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.Test;

class BrowseTheWebTest {

  @Test
  void getBrowserTest1() {
    var browseTheWeb = new BrowseTheWeb();
    Browser browser = browseTheWeb.getBrowser();
    browser.newContext().newPage().navigate("https://shakespeareframework.org");
    browseTheWeb.close();
  }
}
