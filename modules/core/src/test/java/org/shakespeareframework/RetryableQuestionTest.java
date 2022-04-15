package org.shakespeareframework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class RetryableQuestionTest {

  RetryableQuestion<Object> retryableQuestion = (actor) -> true;

  @Test
  @DisplayName("getIgnoredExceptions is empty by default")
  void getIgnoredExceptionsTest1() {
    assertThat(retryableQuestion.getIgnoredExceptions()).isEqualTo(Set.of());
  }

  @ParameterizedTest(name = "acceptable accepts {0} by default")
  @EnumSource(AcceptableAnswerExample.class)
  void acceptableTest1(AcceptableAnswerExample example) {
    assertThat(retryableQuestion.acceptable(example.value)).isTrue();
  }

  enum AcceptableAnswerExample {
    SOMETHING(new Object()),
    PRESENT_OPTIONAL(SOMETHING),
    NON_EMPTY_LIST(List.of(SOMETHING)),
    NON_EMPTY_SET(Set.of(SOMETHING)),
    NON_EMPTY_MAP(Map.of("key", SOMETHING)),
    NON_EMPTY_ARRAY(new Object[] {SOMETHING}),
    SIMPLE_TRUE(true),
    BOXED_TRUE(Boolean.TRUE),
    SOME_STRING("some string");

    final Object value;

    AcceptableAnswerExample(Object value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return name().toLowerCase().replace('_', ' ');
    }
  }

  @ParameterizedTest(name = "acceptable declines {0} by default")
  @EnumSource(UnacceptableAnswerExample.class)
  void acceptableTest2(UnacceptableAnswerExample example) {
    assertThat(retryableQuestion.acceptable(example.value)).isFalse();
  }

  enum UnacceptableAnswerExample {
    NON_PRESENT_OPTIONAL(Optional.empty()),
    EMPTY_LIST(List.of()),
    EMPTY_SET(Set.of()),
    EMPTY_MAP(Map.of()),
    SIMPLY_FALSE(false),
    BOXED_FALSE(Boolean.FALSE),
    NULL(null);

    final Object value;

    UnacceptableAnswerExample(Object value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return name().toLowerCase().replace('_', ' ');
    }
  }
}
