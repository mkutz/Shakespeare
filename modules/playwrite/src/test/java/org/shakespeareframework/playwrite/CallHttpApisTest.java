package org.shakespeareframework.playwrite;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIResponse;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link org.shakespeareframework.Ability Ability} to call HTTP APIs using Playwrite.
 *
 * @see <a href="https://playwright.dev/java/docs/api-testing">Playwrite docs on API testing</a>
 */
class CallHttpApisTest {

  CallHttpApis callHttpApis = new CallHttpApis();
  MockWebServer serviceMock = new MockWebServer();

  @BeforeEach
  void startServiceMock() throws IOException {
    serviceMock.start();
  }

  @Test
  @DisplayName("request can be used to GET a string body")
  void request1() {
    serviceMock.enqueue(new MockResponse().setBody("Hello"));

    final APIResponse response = callHttpApis.request().get(serviceMock.url("/string/").toString());

    assertThat(response.text()).isEqualTo("Hello");
  }

  @Test
  @DisplayName("request can be used to GET a JSON body")
  void request2() {
    serviceMock.enqueue(new MockResponse().setBody("{\"greeting\":\"hello\"}"));

    APIResponse response = callHttpApis.request().get(serviceMock.url("/json/").toString());

    assertThat(new Gson().fromJson(response.text(), JsonObject.class).get("greeting").getAsString())
        .isEqualTo("hello");
  }

  @AfterEach
  void shutdownMockWebServer() throws IOException {
    serviceMock.shutdown();
  }
}
