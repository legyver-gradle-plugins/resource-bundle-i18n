package com.legyver.gradle.resourcebundlei18n.client.response.json;

import com.legyver.gradle.resourcebundlei18n.client.response.decorator.LanguageIdentificationMapDecorator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageIdentificationResponseAdapterTest {
    @Test
    public void parseLanguageFromLibreTranslateResponse() throws Exception {
        String response = "[" +
                "  {" +
                "    \"confidence\": 92," +
                "    \"language\": \"en\"" +
                "  }" +
                "]";
        ListOfMapAdapter jsonResponseMapAdapter = new ListOfMapAdapter();
        FirstOfListMapAdapter firstOfListMapAdapter = new FirstOfListMapAdapter(jsonResponseMapAdapter);
        LanguageIdentificationMapDecorator decorator = new LanguageIdentificationMapDecorator(firstOfListMapAdapter);
        String language = decorator.getIdentifiedLanguage(response);
        assertThat(language).isEqualTo("en");
    }
}
