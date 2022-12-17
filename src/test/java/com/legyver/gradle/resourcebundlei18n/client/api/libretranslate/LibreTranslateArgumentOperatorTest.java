package com.legyver.gradle.resourcebundlei18n.client.api.libretranslate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LibreTranslateArgumentOperatorTest {

    @Test
    public void onePart() {
        String translate = "Hello, {0}!";
        LibreTranslateArgumentOperator toGermanOperator = new LibreTranslateArgumentOperator(translate);
        String englishToGermanTranslatableText = toGermanOperator.getTranslatableText("de");
        assertThat(englishToGermanTranslatableText).isEqualTo(translate);

        String germanTranslation = "Hallo, {0}!";
        String germanResult = toGermanOperator.restoreArgsInTranslatedText(germanTranslation);
        assertThat(germanResult).isEqualTo(germanTranslation);

        LibreTranslateArgumentOperator toSpanishOperator = new LibreTranslateArgumentOperator(translate);
        String englishToSpanishTranslatableText = toSpanishOperator.getTranslatableText("es");
        assertThat(englishToSpanishTranslatableText).isEqualTo("Hello, " + toSpanishOperator.getUntranslatablePlaceholder() + "!");

        String spanishResult = toSpanishOperator.restoreArgsInTranslatedText("¡Hola, " + toSpanishOperator.getUntranslatablePlaceholder() + "!");
        assertThat(spanishResult).isEqualTo("¡Hola, {0}!");
    }

    @Test
    public void twoPart() {
        String translate = "Hello, {0} my {1}!";
        LibreTranslateArgumentOperator toGermanOperator = new LibreTranslateArgumentOperator(translate);
        String englishToGermanTranslatableText = toGermanOperator.getTranslatableText("de");
        assertThat(englishToGermanTranslatableText).isEqualTo(translate);

        String germanTranslation = "Hallo, {0} mein {1}!";
        String germanResult = toGermanOperator.restoreArgsInTranslatedText(germanTranslation);
        assertThat(germanResult).isEqualTo(germanTranslation);

        LibreTranslateArgumentOperator toSpanishOperator = new LibreTranslateArgumentOperator(translate);
        String englishToSpanishTranslatableText = toSpanishOperator.getTranslatableText("es");
        assertThat(englishToSpanishTranslatableText).isEqualTo("Hello, "
                + toSpanishOperator.getUntranslatablePlaceholder() + " my "
                + toSpanishOperator.getUntranslatablePlaceholder() + "!");

        String spanishResult = toSpanishOperator.restoreArgsInTranslatedText("¡Hola, "
                + toSpanishOperator.getUntranslatablePlaceholder() + " mi "
                + toSpanishOperator.getUntranslatablePlaceholder() + "!");
        assertThat(spanishResult).isEqualTo("¡Hola, {0} mi {1}!");
    }

}
