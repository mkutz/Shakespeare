package examples.kotlin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.shakespeareframework.Actor
import org.shakespeareframework.Question
import org.shakespeareframework.retrofit.CallHttpApis
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException

@Execution(ExecutionMode.SAME_THREAD)
class RetrofitDocTest {

  companion object {
    val serviceMock = MockWebServer()
    val serviceUrl = serviceMock.url("/").toString()

    @JvmStatic
    @AfterAll
    @Throws(IOException::class)
    fun shutdownMockWebServer() {
      serviceMock.shutdown()
    }
  }

  // tag::test-api[]
  interface ActorsApi {
    @GET("/actors")
    fun getActors(): Call<List<ActorInfo>>

    @GET("/actors/{id}/name")
    fun getActorName(@Path("id") id: String): Call<String>
  }
  // end::test-api[]

  // tag::scalars-example[]
  data class ActorName(val id: String) : Question<String> {
    override fun answerAs(actor: Actor): String {
      val actorsApiClient = actor
          .uses(CallHttpApis::class.java)
                .buildClient() // <1>
          .baseUrl(serviceUrl) // <2>
          .addScalarsConverterFactory() // <3>
          .build(ActorsApi::class.java) // <4>
      return try {
        actorsApiClient.getActorName(id).execute().body() ?: throw RuntimeException("No body")
      } catch (e: IOException) {
        throw RuntimeException("${this} failed", e)
      }
    }
  }
  // end::scalars-example[]

  @Test
  @Throws(IOException::class)
  fun act1() {
    val ron = Actor("Ron").can(CallHttpApis())
    val jsonBody = """
            [{"id":"273","name":"Alex"},{"id":"476","name":"Jonny"},{"id":"538","name":"Emma"}]
        """
    serviceMock.enqueue(MockResponse().setBody(jsonBody))

    val actors = ron.checks(AllActors())

    assertThat(actors)
      .flatMap(ActorInfo::name)
      .containsExactly("Alex", "Jonny", "Emma")
  }

  @Test
  @Throws(IOException::class)
  fun act2() {
    val scarlet = Actor("Scarlet").can(CallHttpApis())
    serviceMock.enqueue(MockResponse().setBody("Alex"))

    val actorName = scarlet.checks(ActorName("273"))

    assertThat(actorName).isEqualTo("Alex")
  }

  // tag::jackson-example[]
  class AllActors : Question<List<ActorInfo>> {
    override fun answerAs(actor: Actor): List<ActorInfo> {
      val actorsApiClient = actor
          .uses(CallHttpApis::class.java)
                .buildClient()
          .baseUrl(serviceUrl)
          .addJacksonConverterFactory(ObjectMapper().registerModule(kotlinModule())) // <1>
          .build(ActorsApi::class.java)
      return try {
        actorsApiClient.getActors().execute().body() ?: throw RuntimeException("No body")
      } catch (e: IOException) {
        throw RuntimeException("${this} failed", e)
      }
    }
  }

  data class ActorInfo(val id: String, val name: String) // <2>
  // end::jackson-example[]
}
