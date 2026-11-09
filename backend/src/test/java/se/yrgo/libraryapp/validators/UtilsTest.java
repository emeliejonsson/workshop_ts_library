package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    //onlyLettersAndWhiteSpace

    @Test
    void showsOnlyLowercase() {
        assertThat(Utils.onlyLettersAndWhitespace("BO JONSSON")).isEqualTo("bo jonsson");
    }

    @Test
    void removesDigitsAndSymbols() {
        assertThat(Utils.onlyLettersAndWhitespace("B0-Iv4n J0nss0n")).isEqualTo("bivn jnssn");
    }

    @Test
    void emptyString() {
        assertThat(Utils.onlyLettersAndWhitespace("")).isEmpty();
    }

    // cleanAndUntleet

    @Test
    void unleetName() {
        assertThat(Utils.cleanAndUnLeet("B0 J0N550N")).isEqualTo("bo jonsson");
    }

    @ParameterizedTest
    @CsvSource({
            "1, l",
            "3, e",
            "4, a",
            "5, s",
            "6, b",
            "7, t",
            "8, b",
            "0, o"})
    void leetDigitsToLetter(String enteredDigit, String expectedLetter) {
        assertThat(Utils.cleanAndUnLeet(enteredDigit)).isEqualTo(expectedLetter);
    }

    @Test
    void notLeetDigitsRemoved() {
        assertThat(Utils.cleanAndUnLeet("Bo22")).isEqualTo("bo");
    }

}
