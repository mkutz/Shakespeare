package org.shakespeareframework.playwrite;

import com.microsoft.playwright.Browser;

/**
 * {@link org.shakespeareframework.Ability Ability} to browse the web using a Playwrite {@link
 * Browser}.
 *
 * @see <a href="https://playwright.dev/java/docs/intro">Playwrite for Java docs</a>
 */
public class BrowseTheWeb extends UsePlaywrite {

  Browser firefox() {
    return playwrite().firefox().launch();
  }

  Browser chromium() {
    return playwrite().chromium().launch();
  }

  Browser webkit() {
    return playwrite().webkit().launch();
  }
}
