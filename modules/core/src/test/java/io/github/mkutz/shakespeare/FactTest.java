package io.github.mkutz.shakespeare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FactTest {

    @Test
    @DisplayName("getStaticDefault returns the value of DEFAULT field")
    void getStaticDefaultTest1() {
        assertThat(Fact.getStaticDefault(SomeFactWithDefault.class))
                .isPresent()
                .isEqualTo(Optional.of(SomeFactWithDefault.DEFAULT));
    }
    private static class SomeFactWithDefault implements Fact {
        public static final SomeFactWithDefault DEFAULT = new SomeFactWithDefault();
    }

    @Test
    @DisplayName("getStaticDefault returns empty if no DEFAULT is declared")
    void getStaticDefaultTest2() {
        assertThat(Fact.getStaticDefault(SomeFactWithoutDefault.class))
                .isEmpty();
    }
    private static class SomeFactWithoutDefault implements Fact {
    }
}