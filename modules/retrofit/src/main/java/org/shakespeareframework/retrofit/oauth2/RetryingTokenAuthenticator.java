package org.shakespeareframework.retrofit.oauth2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * {@link Authenticator} that will retry authentication at the #tokenServiceUrl for {@link
 * #maxRetries} times.
 */
public abstract class RetryingTokenAuthenticator implements Authenticator {

  /** {@link Oauth2Api} instance to obtain a token. */
  protected final Oauth2Api oauth2Api;

  /** Maximum number of retries. */
  private final int maxRetries;

  /**
   * @param tokenServiceUrl the URl of the token service.
   * @param maxRetries maximum number of retries.
   */
  protected RetryingTokenAuthenticator(String tokenServiceUrl, int maxRetries) {
    this.maxRetries = maxRetries;
    oauth2Api =
        new Retrofit.Builder()
            .baseUrl(tokenServiceUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(Oauth2Api.class);
  }

  @Nullable
  @Override
  public Request authenticate(@Nullable Route route, @Nonnull Response response) {
    if (responseCount(response, 0) >= maxRetries) {
      return null;
    }

    return response
        .request()
        .newBuilder()
        .header("Authorization", String.format("Bearer %s", getToken().getAccessToken()))
        .build();
  }

  /**
   * @return a valid token
   */
  protected abstract Oauth2Token getToken();

  private static int responseCount(final Response response, int count) {
    return response.priorResponse() != null
        ? responseCount(response.priorResponse(), count + 1)
        : count;
  }
}
