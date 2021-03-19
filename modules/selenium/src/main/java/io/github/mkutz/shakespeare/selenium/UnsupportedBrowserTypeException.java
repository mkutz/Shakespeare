package io.github.mkutz.shakespeare.selenium;

/**
 * {@link RuntimeException} to be thrown when the given string did not match any {@link BrowserType}.
 */
public class UnsupportedBrowserTypeException extends RuntimeException {

    /**
     * @param string the string not matching any {@link BrowserType}
     */
    public UnsupportedBrowserTypeException(String string) {
        super("Unknown browser type \"%s\"".formatted(string));
    }
}
