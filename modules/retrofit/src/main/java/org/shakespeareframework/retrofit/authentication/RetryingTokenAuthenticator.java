package org.shakespeareframework.retrofit.authentication;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RetryingTokenAuthenticator implements Authenticator {

  protected final Oauth2Api oauth2Api;

  protected RetryingTokenAuthenticator(String tokenServiceUrl) {
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
    if (responseCount(response, 0) >= 3) {
      return null;
    }

    return response
        .request()
        .newBuilder()
        .header("Authorization", String.format("Bearer %s", getToken().getAccessToken()))
        .build();
  }

  protected abstract Oauth2Token getToken();

  private static int responseCount(final Response response, int count) {
    return response.priorResponse() != null
        ? responseCount(response.priorResponse(), count + 1)
        : count;
  }
}
