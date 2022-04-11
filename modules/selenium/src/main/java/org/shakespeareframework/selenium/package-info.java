/**
 * The central class of the selenium package is the {@link
 * org.shakespeareframework.selenium.BrowseTheWeb} Ability, which requires a {@link
 * org.shakespeareframework.selenium.WebDriverSupplier} to be instantiated.
 *
 * <p>The following suppliers are available too:
 *
 * <ul>
 *   <li>the {@link org.shakespeareframework.selenium.LocalWebDriverSupplier} to use a pre-installed
 *       local browser,
 *   <li>the {@link org.shakespeareframework.selenium.DockerWebDriverSupplier} to use a browser from
 *       an automatically downloaded and started Docker container, and
 *   <li>the {@link org.shakespeareframework.selenium.WebDriverManagerWebDriverSupplier} to use a
 *       self configured {@link io.github.bonigarcia.wdm.WebDriverManager} (the other two extend
 *       this but are pre-configured).
 * </ul>
 *
 * <p>Additionally, the package contains some Reporters:
 *
 * <ul>
 *   <li>the {@link org.shakespeareframework.selenium.ScreenshotReporter} to take screenshots of the
 *       current website, and
 *   <li>the {@link org.shakespeareframework.selenium.HtmlSnapshotReporter} to store an HTML
 *       snapshot of the current website.
 * </ul>
 *
 * <p>Both are {@link org.shakespeareframework.reporting.FileReporter}s and store the report files
 * according to the implementation there.
 *
 * @see <a href="https://shakespeareframework.org/latest/manual/#_browser_automation_with_selenium">
 *     Manual on "Browser Automation with Selenium"</a>
 */
package org.shakespeareframework.selenium;
