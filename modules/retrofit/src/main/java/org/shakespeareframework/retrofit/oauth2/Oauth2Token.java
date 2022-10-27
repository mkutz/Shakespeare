package org.shakespeareframework.retrofit.oauth2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

import static java.time.Instant.now;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Oauth2Token {

  private final String userId;
  private final String accessToken;
  private final Integer expiresIn;
  private final String refreshToken;
  private final Integer refreshExpiresIn;
  private final String scope;
  private final Instant expirationInstant;

  /**
   * @param accessToken the access token {@link String} for the Authorization header
   * @param expiresIn number of seconds before the token expires
   * @param refreshToken the refresh token {@link String}
   * @param refreshExpiresIn number of seconds before the {@link #refreshToken} expires
   * @param scope scope of the {@link #accessToken}
   * @param userId the ID of the user the {@link #accessToken} was issued for
   */
  @JsonCreator
  public Oauth2Token(
      @JsonProperty("access_token") String accessToken,
      @JsonProperty("expires_in") Integer expiresIn,
      @JsonProperty("refresh_token") String refreshToken,
      @JsonProperty("refresh_expires_in") Integer refreshExpiresIn,
      @JsonProperty("scope") String scope,
      @JsonProperty("user_id") String userId) {
    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.expirationInstant = now().plusSeconds(expiresIn);
    this.refreshToken = refreshToken;
    this.refreshExpiresIn = refreshExpiresIn;
    this.scope = scope;
    this.userId = userId;
  }

  /**
   * Checks if this is expired or will be expired in less than 10 seconds.
   *
   * @return true, this is expired or will be expired in less than 10 seconds
   */
  public boolean isExpired() {
    return expirationInstant.isAfter(now().minusSeconds(10));
  }

  public String getAccessToken() {
    return accessToken;
  }
}
