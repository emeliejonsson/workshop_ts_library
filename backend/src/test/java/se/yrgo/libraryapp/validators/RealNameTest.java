package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @Test
    void acceptsNameWithoutBadWords() {
        assertThat(RealName.validate("Bo Jonsson")).isTrue();
    }

    @Test
    void rejectsNameWithBadWordAsFirstName() {
        assertThat(RealName.validate("Badword Bo")).isFalse();
    }

    @Test
    void rejectsNameWithBadWordAsLastName() {
        assertThat(RealName.validate("Bo Frick")).isFalse();
    }

    @Test
    void rejectsNameWithLeetBadWord() {
        assertThat(RealName.validate("Bo D4MN")).isFalse();
    }

}

