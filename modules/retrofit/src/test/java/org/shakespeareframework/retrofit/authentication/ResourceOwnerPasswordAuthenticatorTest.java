package org.shakespeareframework.retrofit.authentication;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.UUID;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResourceOwnerPasswordAuthenticatorTest {

  static MockOAuth2Server mockOAuth2Server = new MockOAuth2Server();
  static final String username = UUID.randomUUID().toString();
  static final String password = UUID.randomUUID().toString();
  static final String clientId = UUID.randomUUID().toString();
  static final String clientSecret = UUID.randomUUID().toString();
  final ResourceOwnerPasswordTokenAuthenticator authenticator =
      new ResourceOwnerPasswordTokenAuthenticator(
          mockOAuth2Server.baseUrl().toString(), username, password, clientId, clientSecret);

  @BeforeAll
  static void startMockOAuth2Server() throws IOException {
    mockOAuth2Server.start();
  }

  @AfterAll
  static void shutdownMockOAuth2Server() throws IOException {
    mockOAuth2Server.shutdown();
  }

  @Test
  @DisplayName("authenticate returns an authenticated version of the original request")
  void test1() {
    var originalRequest = new Request.Builder().url("http://localhost").build();
    var originalResponse =
        new Response.Builder()
            .code(401)
            .message("Authentication failed")
            .request(originalRequest)
            .protocol(Protocol.HTTP_1_1)
            .build();

    authenticator.authenticate(null, originalResponse);

    assertThat(mockOAuth2Server.takeRequest().getBody().readString(UTF_8))
        .contains(
            format("username=%s", username),
            format("password=%s", password),
            format("client_id=%s", clientId),
            format("client_secret=%s", clientSecret),
            "grant_type=password");
  }

  @Test
  @DisplayName("authenticate fails")
  void test2() {
    // TODO
  }
}
