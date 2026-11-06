package se.yrgo.libraryapp.validators;


//returns true if a real name only contains letters and whitespace
//return false if a real name contains non-alphabetic letters

//return true if a real name only contains lowercase

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
        assertThat(RealName.validate("Bo Shiet")).isFalse();
    }

}

// du lämnade på uppgift 3.
