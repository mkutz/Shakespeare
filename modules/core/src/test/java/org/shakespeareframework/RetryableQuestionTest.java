package org.shakespeareframework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RetryableQuestionTest {

  RetryableQuestion<Object> retryableQuestion = (actor) -> true;

  static Object[] acceptableAnswers() {
    var something = new Object();
    return new Object[] {
      Optional.of(something),
      List.of(something),
      Set.of(something),
      Map.of("key", something),
      new Object[] {something},
      true,
      Boolean.TRUE,
      something,
      "some string"
    };
  }

  static Object[] unacceptableAnswers() {
    return new Object[] {
      Optional.empty(), List.of(), Set.of(), Map.of(), false, Boolean.FALSE, null
    };
  }

  @Test
  @DisplayName("getIgnoredExceptions is empty by default")
  void getIgnoredExceptionsTest1() {
    assertThat(retryableQuestion.getIgnoredExceptions()).isEqualTo(Set.of());
  }

  @ParameterizedTest(name = "acceptable accepts {0} by default")
  @MethodSource("acceptableAnswers")
  void acceptableTest1(Object answer) {
    assertThat(retryableQuestion.acceptable(answer)).isTrue();
  }

  @ParameterizedTest(name = "acceptable declines {0} by default")
  @MethodSource("unacceptableAnswers")
  void acceptableTest2(Object answer) {
    assertThat(retryableQuestion.acceptable(answer)).isFalse();
  }

  @Test
  @DisplayName("acceptable declines empty arrays by default")
  void acceptableTest3() {
    assertThat(retryableQuestion.acceptable(new Object[] {})).isFalse();
  }
}
