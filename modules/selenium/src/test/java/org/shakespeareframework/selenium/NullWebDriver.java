package org.shakespeareframework.selenium;

import org.openqa.selenium.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

class NullWebDriver implements WebDriver, TakesScreenshot {

    private boolean closed = false;
    private boolean quit = false;

    @Override
    public void get(String url) {
    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public String getPageSource() {
        return null;
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
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
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
