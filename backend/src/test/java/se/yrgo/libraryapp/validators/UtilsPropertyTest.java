package se.yrgo.libraryapp.validators;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsPropertyTest {

    //onlyLettersAndWhitespace
    @Property
    void resultIsOnlyLettersAndWhitespace(@ForAll String input) {
        String result = Utils.onlyLettersAndWhitespace(input);
        for (char c : result.toCharArray()) {
            assertThat(Character.isLetter(c) || Character.isWhitespace(c)).isTrue();
        }
    }


    @Property
    void resultIsLowercase(@ForAll String input) {
        String result = Utils.onlyLettersAndWhitespace(input);
        assertThat(result).isEqualTo(result.toLowerCase());
    }

    @Test
    void dottedIConvertsToPlainI() {
        assertThat(Utils.onlyLettersAndWhitespace("\u0130")).isEqualTo("i");
    }

    @Property
    void lettersRunTwiceGivesSameResult(@ForAll String input) {
        String result = Utils.onlyLettersAndWhitespace(input);
        assertThat(Utils.onlyLettersAndWhitespace(result)).isEqualTo(result);
    }

    //cleanAndUnleet
    @Provide
    Arbitrary<String> leetStrings() {
        return Arbitraries.strings().withCharRange('a', 'z')
                .withChars('0', '1', '3', '4', '5', '6', '7', '8');
    }

    @Property
    void leetDigitsAreRemoved(@ForAll("leetStrings") String input) {
        String result = Utils.cleanAndUnLeet(input);

        for (char c : result.toCharArray()) {
            assertThat("01345678".indexOf(c)).isEqualTo(-1);
        }
    }

    @Property
    void leetDigitsAreReplacedByLetters(@ForAll("leetStrings") String input) {
        String result = Utils.cleanAndUnLeet(input);

        assertThat(result).hasSameSizeAs(input);
        assertThat(result).matches("[a-z]*");
    }

    @Test
    void leetDigitsAreReplacedWithCorrectLetters() {
        assertThat(Utils.cleanAndUnLeet("01345678")).isEqualTo("oleasbtb");
    }

    @Test
    void nonLettersAreRemoved() {
        assertThat(Utils.cleanAndUnLeet("b0b0!")).isEqualTo("bobo");
    }

    @Property
    void cleanRunTwiceGivesSameReslt(@ForAll String input) {
        String result = Utils.cleanAndUnLeet(input);
        assertThat(Utils.cleanAndUnLeet(result)).isEqualTo(result);
    }

}
