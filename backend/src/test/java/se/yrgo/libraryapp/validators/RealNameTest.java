package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @Test
    void acceptsNameWithoutBadWords() {
        assertThat(RealName.validate("Bo Jonsson")).isTrue();
    }

    @Test
    void rejectsNameWithBadWordAsFirstName() { assertThat(RealName.validate("Badword Bo")).isFalse(); }

    @Test
    void rejectsNameWithBadWordAsLastName() { assertThat(RealName.validate("Bo Shiet")).isFalse(); }

    @Test
    void rejectsNameWithLeetBadWord() { assertThat(RealName.validate("Bo D4MN")).isFalse(); }


    //test the whole file of bad words and rejects them
    static Stream<String> badWordsFromFile() {
        InputStream is = RealName.class.getClassLoader().getResourceAsStream("bad_words.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)); {
            return br.lines();
        }
    }

    @ParameterizedTest
    @MethodSource("badWordsFromFile")
    void rejectsAllBadWordsInFile(String badWord) {
        assertThat(RealName.validate(badWord)).isFalse();
    }

}
