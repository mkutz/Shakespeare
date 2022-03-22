package org.shakespeareframework.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BrowserTypeTest {

  @ParameterizedTest(name = "forName(\"{0}\")")
  @ValueSource(
      strings = {
        "chrome", "CHROME", "Chrome",
        "firefox", "FIREFOX", "Firefox",
        "opera", "OPERA", "Opera",
        "edge", "EDGE", "Edge",
        "iexplorer", "IEXPLORER", "IExplorer"
      })
  void forNameTest1(String string) {
    assertThat(BrowserType.forName(string)).isNotNull();
  }

  @ParameterizedTest(name = "forName(\"{0}\") throws UnsupportedBrowserTypeException")
  @ValueSource(
      strings = {
        "google-chrome",
        "internet-explorer",
        "apple-safari",
        "ie",
        "ff",
        "mozilla",
        "ms-edge",
        "uwiqe",
        "brave",
        "phantom"
      })
  void forNameTest2(String string) {
    assertThatExceptionOfType(UnsupportedBrowserTypeException.class)
        .isThrownBy(() -> BrowserType.forName(string));
  }
}
