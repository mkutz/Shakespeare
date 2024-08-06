package org.shakespeareframework.retrofit.oauth2;

import static java.time.Instant.now;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/** A parsed OAuth2 token. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Oauth2Token {

  private final String accessToken;
  private final Instant expirationInstant;

  /**
   * @param accessToken the access token {@link String} for the Authorization header
   * @param expiresIn number of seconds before the token expires
   */
  @JsonCreator
  public Oauth2Token(
      @JsonProperty("access_token") String accessToken,
      @JsonProperty("expires_in") Integer expiresIn) {
    this.accessToken = accessToken;
    this.expirationInstant = now().plusSeconds(expiresIn);
  }

  /**
   * Checks if this is expired or will be expired in less than 10 seconds.
   *
   * @return true, this is expired or will be expired in less than 10 seconds
   */
  public boolean isExpired() {
    return now().plusSeconds(10).isAfter(expirationInstant);
  }

  /**
   * @return the access token {@link String} for the Authorization header
   */
  public String getAccessToken() {
    return accessToken;
  }
}
