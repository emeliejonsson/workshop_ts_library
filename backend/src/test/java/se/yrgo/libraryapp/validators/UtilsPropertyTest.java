package se.yrgo.libraryapp.validators;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsPropertyTest {

    //egenskap - ett påstående som ska vara sant för alla giltiga indata
    //@Property ersätter @Test - säger att testet ska köras flera gånger
    //@ForALl = Jqwik genererar slumpmässiga värden för den här parametern

    //För onlyLettersAndWhitespace() kan du ju helt anropa den med slumpmässig
    // data från jqwik och
    //kontrollera genom att loopa genom resultatsträngen att ingenting
    // är annat än små bokstäver och whitespace.
    //
    //För cleanAndUnLeet() kan du t.ex. pröva med att skapa en @Provider
    // som skapar textsträngar med leet-code i som sedan inte skall vara kvar.
    //
    //Fundera över vilka tester som är lämpliga för dessa två metoder
    // och implementera dem. Hittar du några buggar meddelar du mig och rättar dem.

    //onlyLettersAndWhitespace
    @Property
    void resultIsOnlyLettersAndWhitespace(@ForAll String input) {
        String result = Utils.onlyLettersAndWhitespace(input);
        for (char c : result.toCharArray()) {
            assertThat(Character.isLetter(c) || Character.isWhitespace(c)).isTrue();
        }
    }

    @Test
    void onlyLettersAndWhiteSpaceHandlesIDot() {
        assertThat(Utils.onlyLettersAndWhitespace("\u0130")).isEqualTo("i");
    }

    @Property
    void onlyLettersAndWhitespaceReturnsLowercase(@ForAll String input) {
        String result = Utils.onlyLettersAndWhitespace(input);
        assertThat(result).isEqualTo(result.toLowerCase());
    }

    @Property
    void resultIsTheSame(@ForAll String input) {
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




}
