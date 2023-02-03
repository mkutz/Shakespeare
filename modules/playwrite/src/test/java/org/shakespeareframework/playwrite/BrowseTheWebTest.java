package org.shakespeareframework.playwrite;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BrowseTheWebTest {

  @Test
  @DisplayName("firefox returns a working Browser instance")
  void firefox() {
    try (var browseTheWeb = new BrowseTheWeb()) {
      var browser = browseTheWeb.firefox();
      var page = browser.newPage();

      page.navigate("https://shakespeareframework.org");

      assertThat(page).hasTitle("Shakespeare Framework | Shakespeare");
    }
  }

  @Test
  @DisplayName("chromium returns a working Browser instance")
  void chromium() {
    try (var browseTheWeb = new BrowseTheWeb()) {
      var browser = browseTheWeb.chromium();
      var page = browser.newPage();

      page.navigate("https://shakespeareframework.org");

      assertThat(page).hasTitle("Shakespeare Framework | Shakespeare");
    }
  }

  @Test
  @DisplayName("webkit returns a working Browser instance")
  void webkit() {
    try (var browseTheWeb = new BrowseTheWeb()) {
      var browser = browseTheWeb.webkit();
      var page = browser.newPage();

      page.navigate("https://shakespeareframework.org");

      assertThat(page).hasTitle("Shakespeare Framework | Shakespeare");
    }
  }
}
