package org.shakespeareframework.retrofit.oauth2;

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
  private final String scope;
  private Oauth2Token token;

  /**
   * @param tokenServiceUrl the URl of the token service.
   * @param username the username
   * @param password the password
   * @param clientId the client ID
   * @param clientSecret the client secret
   * @param scope the scope
   */
  public ResourceOwnerPasswordTokenAuthenticator(
      String tokenServiceUrl,
      String username,
      String password,
      String clientId,
      String clientSecret,
      String scope) {
    super(tokenServiceUrl, 3);
    this.username = username;
    this.password = password;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.scope = scope;
  }

  @Override
  protected Oauth2Token getToken() {
    if (token == null || token.isExpired()) {
      try {
        final var response =
            oauth2Api
                .getToken(clientId, clientSecret, username, password, scope, "password")
                .execute();

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
