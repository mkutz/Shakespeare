package org.shakespeareframework.retrofit.oauth2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/** Definition of the OAuth2 protocol token endpoints */
interface Oauth2Api {

  @FormUrlEncoded
  @POST("token")
  Call<Oauth2Token> getToken(
      @Field("client_id") String clientId,
      @Field("client_secret") String clientSecret,
      @Field("username") String username,
      @Field("password") String password,
      @Field("scope") String scope,
      @Field("grant_type") String grantType);

  @FormUrlEncoded
  @POST("token")
  Call<Oauth2Token> getToken(
      @Field("client_id") String clientId,
      @Field("client_secret") String clientSecret,
      @Field("scope") String scope,
      @Field("grant_type") String grantType);
}
