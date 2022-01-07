package org.shakespeareframwork.retrofit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.shakespeareframework.Ability;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * {@link Ability} to call HTTP APIs using {@link Retrofit}.
 */
public class CallHttpApis implements Ability {

    /**
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
         * @param baseUrl base URL of the API
         * @return the {@link Builder}
         */
        public Builder baseUrl(String baseUrl) {
            retrofitBuilder.baseUrl(baseUrl);
            return this;
        }

        /**
         * @param converterFactory a {@link Converter.Factory} to be added to the {@link #retrofitBuilder}
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
            return addConverterFactory(JacksonConverterFactory.create());
        }

        /**
         * @param interceptor an {@link Interceptor} to be added to the {@link #okHttpClientBuilder}
         * @return the {@link Builder}
         * @see OkHttpClient.Builder#addInterceptor(Interceptor)
         */
        public Builder addInterceptor(Interceptor interceptor) {
            okHttpClientBuilder.addInterceptor(interceptor);
            return this;
        }

        /**
         * @param clientClass the API client class
         * @param <C>         the type of the API client class
         * @return an instance of clientClass to interact with the API
         */
        public <C> C build(Class<? extends C> clientClass) {
            return retrofitBuilder
                    .client(okHttpClientBuilder.build())
                    .build()
                    .create(clientClass);
        }
    }
}
