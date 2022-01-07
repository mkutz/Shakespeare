package org.shakespeareframwork.retrofit;

import retrofit2.Response;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Exception to be thrown for a failed response.
 */
public class ResponseException extends RuntimeException {

    /** The full {@link Response} object */
    Response<?> response;

    /** The {@link Response}'s body */
    Object body;

    /** A {@link Map} of all the {@link Response}'s headers. */
    Map<String, ?> headers;

    /**
     * @param message the exception message.
     * @param response the {@link Response}
     */
    public ResponseException(final String message, final Response<?> response) {
        super(message);
        this.response = response;
        this.body = response.body();
        this.headers = response.headers().toMultimap();
    }
}
