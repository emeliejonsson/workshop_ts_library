package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class UsernameTest {
    @Test
    void acceptsSimpleLowerCaseUsername() {
        assertThat(Username.validate("bosse")).isTrue();
    }

    @Test
    void rejectsUsernameWithSpaces() {
        assertThat(Username.validate("name with space")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"bo sse", "bosse_bo!"})
    void rejectsInvalidUsernames(String input) {
        assertThat(Username.validate(input)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"bo", "boo", "oBo"})
    void rejectsUsernamesShorterThanFourChar(String input) {
        assertThat(Username.validate(input)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"bobo", "boss"})
    void acceptsUsernameThatAreFourCharsLong(String input) {
        assertThat(Username.validate(input)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"bosse", "b0b0", "bo@orm", "bobo_boy", "bo-bo"})
    void acceptsValidUsernames(String input) {
        assertThat(Username.validate(input)).isTrue();
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"\t", "\n"})
    void rejectsEmptyAndOnlyWhitespaceUsernames(String input) {
        assertThat(Username.validate(input)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"bosse0", "bosse1", "bosse2", "bosse3", "bosse4", "bosse5", "bosse6", "bosse7", "bosse8", "bosse9"})
    void acceptsUsernamesEndingInAnyDigit(String input) {
        assertThat(Username.validate(input)).isTrue();
    }
}
