package org.shakespeareframework.playwrite;

import com.microsoft.playwright.APIRequestContext;

/** {@link org.shakespeareframework.Ability} to */
public class CallHttpApis extends UsePlaywrite {

  public APIRequestContext request() {
    return playwrite().request().newContext();
  }
}
