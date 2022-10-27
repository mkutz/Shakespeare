package org.shakespeareframework.retrofit.oauth2;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RetryingTokenAuthenticatorTest {

  StaticRetryingTokenAuthenticator retryingTokenAuthenticator =
      new StaticRetryingTokenAuthenticator();

  @Test
  @DisplayName("authenticate gets a token and adds it to the request")
  void test1() {
    var originalRequest = new Request.Builder().url("http://localhost").build();
    var originalResponse =
        new Response.Builder()
            .code(401)
            .message("Authentication failed")
            .request(originalRequest)
            .protocol(Protocol.HTTP_1_1)
            .build();

    var authenticatedRequest = retryingTokenAuthenticator.authenticate(null, originalResponse);

    assertThat(authenticatedRequest).isNotNull();
    assertThat(authenticatedRequest.header("Authorization"))
        .isNotBlank()
        .startsWith(format("Bearer %s", retryingTokenAuthenticator.token.getAccessToken()));
    assertThat(authenticatedRequest.headers()).containsAll(originalRequest.headers());
    assertThat(authenticatedRequest.method()).isEqualTo(originalRequest.method());
    assertThat(authenticatedRequest.url()).isEqualTo(originalRequest.url());
    assertThat(authenticatedRequest.body()).isEqualTo(originalRequest.body());
  }

  @Test
  @DisplayName("authenticate retries up three times")
  void test2() {
    var originalRequest = new Request.Builder().url("http://localhost").build();
    var firstResponse =
        new Response.Builder()
            .code(401)
            .message("Authentication failed")
            .request(originalRequest)
            .protocol(Protocol.HTTP_1_1)
            .build();
    var secondResponse = firstResponse.newBuilder().priorResponse(firstResponse).build();
    var thirdResponse = secondResponse.newBuilder().priorResponse(secondResponse).build();

    var authenticatedRequest = retryingTokenAuthenticator.authenticate(null, thirdResponse);

    assertThat(authenticatedRequest).isNotNull();
  }

  @Test
  @DisplayName("authenticate retries up three times")
  void test3() {
    var originalRequest = new Request.Builder().url("http://localhost").build();
    var firstResponse =
        new Response.Builder()
            .code(401)
            .message("Authentication failed")
            .request(originalRequest)
            .protocol(Protocol.HTTP_1_1)
            .build();
    var secondResponse = firstResponse.newBuilder().priorResponse(firstResponse).build();
    var thirdResponse = secondResponse.newBuilder().priorResponse(secondResponse).build();
    var fourthResponse = thirdResponse.newBuilder().priorResponse(thirdResponse).build();

    var authenticatedRequest = retryingTokenAuthenticator.authenticate(null, fourthResponse);

    assertThat(authenticatedRequest).isNull();
  }

  static class StaticRetryingTokenAuthenticator extends RetryingTokenAuthenticator {

    final Oauth2Token token;

    protected StaticRetryingTokenAuthenticator() {
      super("http://localhost", 3);
      token = new Oauth2Token(randomUUID().toString(), 10000);
    }

    @Override
    protected Oauth2Token getToken() {
      return token;
    }
  }
}
