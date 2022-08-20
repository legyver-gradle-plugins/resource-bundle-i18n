package com.legyver.gradle.resourcebundlei18n.client.response.json;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonResponseMapAdapterTest {

    private final JsonResponseMapAdapter jsonResponseMapAdapter = new JsonResponseMapAdapter();

    @Test
    public void adaptLibreTranslateLanguageIdentificationResponse() throws Exception {
        String response =
                "{" +
                "    \"confidence\": 92," +
                "    \"language\": \"en\"" +
                "}";
        Map<String, Object> adapted = jsonResponseMapAdapter.adapt(response);
        assertThat(adapted).hasSize(2);
        assertThat(adapted.get("confidence")).isEqualTo(92);
        assertThat(adapted.get("language")).isEqualTo("en");
    }

    @Test
    public void adaptLibreTranslateTranslateResponse() throws Exception {
        String response = "{" +
                "  \"translatedText\": \"hola\"" +
                "}";
        Map<String, Object> adapted = jsonResponseMapAdapter.adapt(response);
        assertThat(adapted).hasSize(1);
        assertThat(adapted.get("translatedText")).isEqualTo("hola");
    }

    @Test
    public void adaptLibreTranslateTranslateResponseWithCommas() throws Exception {
        String response = "{" +
                "  \"translatedText\": \"hola, senor\"" +
                "}";
        Map<String, Object> adapted = jsonResponseMapAdapter.adapt(response);
        assertThat(adapted).hasSize(1);
        assertThat(adapted.get("translatedText")).isEqualTo("hola, senor");
    }

    @Test void adaptTranslationResponseWithNoWhitespaceSeparators() throws Exception {
        String response = "{\"translatedText\": \"hola, senor\"}";
        Map<String, Object> adapted = jsonResponseMapAdapter.adapt(response);
        assertThat(adapted).hasSize(1);
        assertThat(adapted.get("translatedText")).isEqualTo("hola, senor");
    }

    @Test
    public void adaptResponseWithNullValue() throws Exception {
        String response = "{" +
                "  \"myvar\": null" +
                "}";
        Map<String, Object> adapted = jsonResponseMapAdapter.adapt(response);
        assertThat(adapted).hasSize(1);
        assertThat(adapted.get("myvar")).isNull();
    }
}
