package com.legyver.gradle.resourcebundlei18n.client.api.libretranslate;

import com.legyver.gradle.resourcebundlei18n.client.api.DetectLanguageApiStrategy;
import com.legyver.gradle.resourcebundlei18n.client.api.TranslationApiStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LibreTranslationApiTest {

    @Test
    public void getAcceptedTypeForDetectionRequest() {
        String expected = "application/json";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new DetectLanguageApiStrategy());
        assertThat(libreTranslationApi.getAcceptedType()).isEqualTo(expected);
    }

    @Test
    public void getContentTypeForDetectionRequest() {
        String expected = "application/json";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new DetectLanguageApiStrategy());
        assertThat(libreTranslationApi.getContentType()).isEqualTo(expected);
    }

    @Test
    public void makeRequestBodyContentForDetectionRequest() {
        String expected = "{\"q\": \"hello\", \"api_key\": \"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\"}";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new DetectLanguageApiStrategy());
        assertThat(libreTranslationApi.makeRequestBody("hello")).isEqualTo(expected);
    }

    @Test
    public void getLanguageFromDetectionResponse() throws Exception {
        String expected = "en";
        String response =
                  "[{" +
                  "    \"confidence\": 92," +
                  "    \"language\": \"en\"" +
                  "}]";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new DetectLanguageApiStrategy());
        assertThat(libreTranslationApi.getResult(response)).isEqualTo(expected);
    }

    @Test
    public void getAcceptedTypeForTranslationRequest() {
        String expected = "application/json";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new TranslationApiStrategy());
        assertThat(libreTranslationApi.getAcceptedType()).isEqualTo(expected);
    }

    @Test
    public void getContentTypeForTranslationRequest() {
        String expected = "application/json";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new TranslationApiStrategy());
        assertThat(libreTranslationApi.getContentType()).isEqualTo(expected);
    }

    @Test
    public void makeRequestBodyContentForTranslationRequest() {
        String expected = "{\"q\": \"hello\", \"source\": \"en\", \"target\": \"es\", \"format\": \"text\", \"api_key\": \"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\"}";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new TranslationApiStrategy());
        assertThat(libreTranslationApi.makeRequestBody("hello", "en", "es")).isEqualTo(expected);
    }

    @Test
    public void getLanguageFromTranslationResponse() throws Exception {
        String expected = "hola";
        String response = "{\n" +
                "  \"translatedText\": \"hola\"\n" +
                "}";
        LibreTranslationApi libreTranslationApi = new LibreTranslationApi();
        libreTranslationApi.setStrategy(new TranslationApiStrategy());
        assertThat(libreTranslationApi.getResult(response)).isEqualTo(expected);
    }



}
