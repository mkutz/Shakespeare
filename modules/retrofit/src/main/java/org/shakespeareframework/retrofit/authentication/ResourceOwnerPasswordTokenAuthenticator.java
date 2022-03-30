package org.shakespeareframework.retrofit.authentication;

import java.io.IOException;

/**
 * {@link RetryingTokenAuthenticator} to automatically authenticate with the {@link Oauth2Api} via
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-1.3.3">resource owner password
 * flow</a> (grant type {@code password}) and add the access token as a header.
 *
 * <p>Also note that refresh tokens are currently ignored!
 */
public class ResourceOwnerPasswordTokenAuthenticator extends RetryingTokenAuthenticator {

  private final String username;
  private final String password;
  private final String clientId;
  private final String clientSecret;
  private Oauth2Token token;

  public ResourceOwnerPasswordTokenAuthenticator(
      String tokenServiceUrl,
      String username,
      String password,
      String clientId,
      String clientSecret) {
    super(tokenServiceUrl);
    this.username = username;
    this.password = password;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  @Override
  protected Oauth2Token getToken() {
    if (token == null || token.isExpired()) {
      try {
        final var response =
            oauth2Api.getToken(clientId, clientSecret, username, password, "password").execute();

        if (!response.isSuccessful() || response.body() == null) {
          throw new Oauth2AuthenticationFailedException();
        }

        token = response.body();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return token;
  }
}
