package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @Test
    void acceptsName() {
        assertThat(RealName.validate("Bo Jonsson")).isTrue();
    }

    @Test
    void rejectsFirstBadWord() {
        assertThat(RealName.validate("Frick Bo")).isFalse();
    }

    @Test
    void rejectsSecondBadWord() {
        assertThat(RealName.validate("Bo Frick")).isFalse();
    }

    @Test
    void rejectsLeetBadWord() {
        assertThat(RealName.validate("Bo D4MN")).isFalse();
    }

}

