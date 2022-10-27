package org.shakespeareframework.retrofit.oauth2;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.UUID;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class Oauth2ApiTest {

  static final MockOAuth2Server mockOAuth2Server = new MockOAuth2Server();

  @BeforeAll
  static void startMockOAuth2Server() {
    mockOAuth2Server.start();
  }

  @AfterAll
  static void shutdownMockOAuth2Server() {
    mockOAuth2Server.shutdown();
  }

  @Test
  void test1() throws IOException {
    var oauth2Api =
        new Retrofit.Builder()
            .baseUrl(mockOAuth2Server.baseUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(Oauth2Api.class);

    var response = oauth2Api
        .getToken(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), "password")
        .execute();

    assertThat(response.code()).isEqualTo(200);
    assertThat(response.body()).isNotNull();
  }
}
