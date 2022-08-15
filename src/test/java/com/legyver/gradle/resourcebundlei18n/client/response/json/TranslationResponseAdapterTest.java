package com.legyver.gradle.resourcebundlei18n.client.response.json;

import com.legyver.gradle.resourcebundlei18n.client.response.decorator.TranslationMapDecorator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TranslationResponseAdapterTest {

    @Test
    public void parseLanguageFromLibreTranslateResponse() {
        String response = "{\n" +
                "  \"translatedText\": \"hola\"\n" +
                "}";
        JsonResponseMapAdapter jsonResponseMapAdapter = new JsonResponseMapAdapter();
        TranslationMapDecorator decorator = new TranslationMapDecorator(jsonResponseMapAdapter);
        String translation = decorator.getTranslation(response);
        assertThat(translation).isEqualTo("hola");
    }
}
