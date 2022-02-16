package org.shakespeareframework.retrofit;

import okhttp3.Interceptor;
import okhttp3.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Interceptor} to add headers to any request. This should be used to set authentication or other contextual
 * headers.
 */
public class HeaderInterceptor implements Interceptor {

    private final Map<String, String> headers = new HashMap<>();

    @Override
    @Nonnull
    public Response intercept(Chain chain) throws IOException {
        final var originalRequest = chain.request();
        if (headers.isEmpty()) {
            return chain.proceed(originalRequest);
        }

        final var requestBuilder = originalRequest.newBuilder();
        headers.forEach(requestBuilder::addHeader);
        return chain.proceed(requestBuilder.build());
    }

    /**
     * Adds a header that will be added to the intercepted request.
     *
     * @param key   name of the header to be added
     * @param value value of the header to be added
     */
    public void add(String key, String value) {
        this.headers.put(key, value);
    }

    /**
     * Removes a header that will no longer be added to the intercepted request.
     *
     * @param key name of the header to be removed
     */
    public void remove(String key) {
        headers.remove(key);
    }
}
