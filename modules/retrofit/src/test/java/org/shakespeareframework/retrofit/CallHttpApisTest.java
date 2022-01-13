package org.shakespeareframework.retrofit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CallHttpApisTest {

    CallHttpApis callRestApis = new CallHttpApis();
    MockWebServer serviceMock = new MockWebServer();

    @BeforeEach
    void startServiceMock() throws IOException {
        serviceMock.start();
    }

    @Test
    @DisplayName("buildClient addScalarsConverterFactory")
    void buildClientWithScalarsConverterFactoryTest1() throws IOException {
        final var client = callRestApis.buildClient()
                .addScalarsConverterFactory()
                .baseUrl(serviceMock.url("/string/").toString())
                .build(TestApi.class);
        serviceMock.enqueue(new MockResponse().setBody("Hello"));

        final Response<String> response = client.getString().execute();

        assertThat(response.body()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("buildClient addJacksonConverterFactory")
    void buildClientWithJacksonConverterFactoryTest1() throws IOException {
        final var client = callRestApis.buildClient()
                .addJacksonConverterFactory()
                .baseUrl(serviceMock.url("/json/").toString())
                .build(TestApi.class);
        serviceMock.enqueue(new MockResponse().setBody("{\"greeting\":\"hello\"}"));

        final Response<TestApi.JsonResponseBody> response = client.getJson().execute();

        assertThat(response.body()).isEqualTo(new TestApi.JsonResponseBody("hello"));
    }

    @Test
    @DisplayName("buildClient addInterceptor")
    void buildClientWithInterceptor() throws IOException, InterruptedException {
        final var headerInterceptor = new HeaderInterceptor();
        final var client = callRestApis.buildClient()
                .addInterceptor(headerInterceptor)
                .addScalarsConverterFactory()
                .baseUrl(serviceMock.url("/string/").toString())
                .build(TestApi.class);

        serviceMock.enqueue(new MockResponse().setBody("Hello"));
        client.getString().execute();
        assertThat(serviceMock.takeRequest().getHeader("test")).isNull();

        serviceMock.enqueue(new MockResponse().setBody("Hello"));
        headerInterceptor.add("test-header", "test");
        client.getString().execute();
        assertThat(serviceMock.takeRequest().getHeader("test-header")).isEqualTo("test");

        serviceMock.enqueue(new MockResponse().setBody("Hello"));
        headerInterceptor.remove("test-header");
        client.getString().execute();
        assertThat(serviceMock.takeRequest().getHeader("test")).isNull();
    }

    @AfterEach
    void shutdownMockWebServer() throws IOException {
        serviceMock.shutdown();
    }

    interface TestApi {

        @GET("/string")
        Call<String> getString();

        @GET("/json")
        Call<JsonResponseBody> getJson();

        record JsonResponseBody(String greeting) {}
    }
}
