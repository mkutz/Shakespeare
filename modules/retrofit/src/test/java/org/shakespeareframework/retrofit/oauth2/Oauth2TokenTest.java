package org.shakespeareframework.retrofit.oauth2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Oauth2TokenTest {

  @ParameterizedTest(name = "isExpired is false for expiresIn = {0} (> 10)")
  @ValueSource(ints = {11, 6_000})
  void isExpiredTest1(int expiresIn) {
    assertThat(new Oauth2Token(UUID.randomUUID().toString(), expiresIn).isExpired()).isFalse();
  }

  @ParameterizedTest(name = "isExpired is true for expiresIn = {0} (<= 10)")
  @ValueSource(ints = {-6_000, 0, 10})
  void isExpiredTest2(int expiresIn) {
    assertThat(new Oauth2Token(UUID.randomUUID().toString(), expiresIn).isExpired()).isTrue();
  }
}
