import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Question;
import org.shakespeareframework.retrofit.CallHttpApis;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Execution(ExecutionMode.SAME_THREAD)
class RetrofitDocTest {

  static MockWebServer serviceMock = new MockWebServer();
  static String ACTORS_SERVICE_URL = serviceMock.url("/").toString();

  @AfterAll
  static void shutdownMockWebServer() throws IOException {
    serviceMock.shutdown();
  }

  // tag::test-api[]
  interface ActorsApi {

    @GET("/actors")
    Call<List<ActorInfo>> getActors();

    @GET("/actors/{id}/name")
    Call<String> getActorName(@Path("id") String id);
  }
  // end::test-api[]

  // tag::scalars-example[]
  record ActorName(String id) implements Question<String> {

    @Override
    public String answerAs(Actor actor) {
      var actorsApiClient =
          actor
              .uses(CallHttpApis.class)
              .buildClient() // <1>
              .baseUrl(ACTORS_SERVICE_URL) // <2>
              .addScalarsConverterFactory() // <3>
              .build(ActorsApi.class); // <4>
      try {
        return actorsApiClient.getActorName(id).execute().body();
      } catch (IOException e) {
        throw new RuntimeException("%s failed".formatted(this), e);
      }
    }
  }
  // end::scalars-example[]

  @Test
  void act1() {
    Actor ron = new Actor("Ron").can(new CallHttpApis());
    var jsonBody =
        """
                [{"id":"273","name":"Alex"},{"id":"476","name":"Jonny"},{"id":"538","name":"Emma"}]
                """;
    serviceMock.enqueue(new MockResponse().setBody(jsonBody));

    var actors = ron.checks(new AllActors());

    assertThat(actors).map(ActorInfo::name).containsExactly("Alex", "Jonny", "Emma");
  }

  @Test
  void act2() {
    Actor scarlet = new Actor("Scarlet").can(new CallHttpApis());
    serviceMock.enqueue(new MockResponse().setBody("Alex"));

    var actors = scarlet.checks(new ActorName("273"));

    assertThat(actors).isEqualTo("Alex");
  }

  // tag::jackson-example[]
  record AllActors() implements Question<List<ActorInfo>> {

    @Override
    public List<ActorInfo> answerAs(Actor actor) {
      var actorsApiClient =
          actor
              .uses(CallHttpApis.class)
              .buildClient()
              .baseUrl(ACTORS_SERVICE_URL)
              .addJacksonConverterFactory() // <1>
              .build(ActorsApi.class);
      try {
        return actorsApiClient.getActors().execute().body();
      } catch (IOException e) {
        throw new RuntimeException("%s failed".formatted(this), e);
      }
    }
  }

  record ActorInfo(String id, String name) {} // <2>
  // end::jackson-example[]
}
