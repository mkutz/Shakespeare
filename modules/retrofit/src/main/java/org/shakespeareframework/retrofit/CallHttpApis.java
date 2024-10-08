package org.shakespeareframework.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.jspecify.annotations.NullMarked;
import org.shakespeareframework.Ability;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/** {@link Ability} to call RESTful APIs using {@link Retrofit}. */
@NullMarked
public class CallHttpApis implements Ability {

  /**
   * Creates a new {@link Builder}.
   *
   * @return a new {@link Builder}
   */
  public Builder buildClient() {
    return new Builder();
  }

  /**
   * Builder wrapping a {@link Retrofit.Builder} and a {@link OkHttpClient.Builder} to allow setting
   * both with one class.
   */
  public static class Builder {

    private final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    private final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

    private Builder() {}

    /**
     * Sets the base URL to the {@link #retrofitBuilder}.
     *
     * @param baseUrl base URL of the API
     * @return the {@link Builder}
     * @see Retrofit.Builder#baseUrl(String)
     */
    public Builder baseUrl(String baseUrl) {
      retrofitBuilder.baseUrl(baseUrl);
      return this;
    }

    /**
     * Adds the given {@link Converter.Factory} to the {@link #retrofitBuilder}.
     *
     * @param converterFactory a {@link Converter.Factory} to be added to the {@link
     *     #retrofitBuilder}
     * @return the {@link Builder}
     * @see Retrofit.Builder#addConverterFactory(Converter.Factory)
     */
    public Builder addConverterFactory(Converter.Factory converterFactory) {
      retrofitBuilder.addConverterFactory(converterFactory);
      return this;
    }

    /**
     * Adds a {@link ScalarsConverterFactory} to the {@link #retrofitBuilder}.
     *
     * @return the {@link Builder}
     * @see #addConverterFactory(Converter.Factory)
     * @see ScalarsConverterFactory
     */
    public Builder addScalarsConverterFactory() {
      return addConverterFactory(ScalarsConverterFactory.create());
    }

    /**
     * Adds a {@link JacksonConverterFactory} to the {@link #retrofitBuilder}.
     *
     * @return the {@link Builder}
     * @see #addConverterFactory(Converter.Factory)
     * @see JacksonConverterFactory
     */
    public Builder addJacksonConverterFactory() {
      return addJacksonConverterFactory(new ObjectMapper());
    }

    /**
     * Adds a {@link JacksonConverterFactory} using the given objectMapper to the {@link
     * #retrofitBuilder}.
     *
     * @param objectMapper the {@link ObjectMapper} to be used by the {@link
     *     JacksonConverterFactory}
     * @return the {@link Builder}
     * @see #addConverterFactory(Converter.Factory)
     * @see JacksonConverterFactory
     */
    public Builder addJacksonConverterFactory(ObjectMapper objectMapper) {
      return addConverterFactory(JacksonConverterFactory.create(objectMapper));
    }

    /**
     * Adds the given {@link Interceptor} to the {@link #okHttpClientBuilder}.
     *
     * @param interceptor an {@link Interceptor} to be added to the {@link #okHttpClientBuilder}
     * @return the {@link Builder}
     * @see OkHttpClient.Builder#addInterceptor(Interceptor)
     */
    public Builder addInterceptor(Interceptor interceptor) {
      okHttpClientBuilder.addInterceptor(interceptor);
      return this;
    }

    /**
     * Sets the {@link Authenticator} of the {@link #okHttpClientBuilder}.
     *
     * @param authenticator the {@link Authenticator} to be set
     * @return the {@link Builder}
     * @see OkHttpClient.Builder#authenticator(Authenticator)
     */
    public Builder authenticator(Authenticator authenticator) {
      okHttpClientBuilder.authenticator(authenticator);
      return this;
    }

    /**
     * Finalizes the build and returns the client.
     *
     * @param clientClass the API client class
     * @param <C> the type of the API client class
     * @return an instance of clientClass to interact with the API
     * @see Retrofit.Builder#build()
     * @see OkHttpClient.Builder#build()
     * @see Retrofit#create(Class)
     */
    public <C> C build(Class<? extends C> clientClass) {
      return retrofitBuilder.client(okHttpClientBuilder.build()).build().create(clientClass);
    }
  }
}
