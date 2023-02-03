package org.shakespeareframework.playwrite;

import com.microsoft.playwright.Playwright;
import org.shakespeareframework.Ability;

/**
 * {@link Ability} to use Playwrite.
 *
 * @see <a href="https://playwright.dev/java/docs/intro">Playwrite for Java docs</a>
 */
public class UsePlaywrite implements Ability, AutoCloseable {

  private Playwright playwright;

  Playwright playwrite() {
    if (playwright == null) {
      playwright = Playwright.create();
    }
    return playwright;
  }

  @Override
  public void close() {
    if (playwright != null) {
      playwright.close();
    }
  }
}
