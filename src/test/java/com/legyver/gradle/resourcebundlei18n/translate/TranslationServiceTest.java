package com.legyver.gradle.resourcebundlei18n.translate;

import com.legyver.core.exception.CoreException;
import com.legyver.gradle.resourcebundlei18n.TranslationClientType;
import com.legyver.gradle.resourcebundlei18n.client.Client;
import com.legyver.gradle.resourcebundlei18n.client.api.TranslationApi;
import com.legyver.utils.propl.PropertyList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class TranslationServiceTest {
    private static TranslationService translationService;

    @BeforeAll
    public static void setUp() throws MalformedURLException {
        translationService = new TranslationService();
        TranslationApi translationApi = TranslationClientType.LIBRETRANSLATE.getTranslationApi(null);
        translationService.setClient(new Client(new URL("http://localhost:5000"), translationApi));
    }

    private void loadAndTest(TestScenario testScenario, Consumer<PropertyList> asserter) throws IOException, CoreException {
        PropertyList properties = new PropertyList();
        try (InputStream inputStream = TranslationServiceTest.class.getResourceAsStream(testScenario.propertyFile)) {
            properties.load(inputStream);
            PropertyList translateProperties = translationService.translateProperties(properties, testScenario.sourceLanguage, testScenario.sourceCountry, testScenario.targetLanguage);
            asserter.accept(translateProperties);
        }
    }

    @Test
    public void translateEnglishUSToGB() throws Exception {
        loadAndTest(new TestScenario("test_en_US.properties")
                        .sourceLanguage("en")
                        .sourceCountry("US")
                        .targetLanguage("en_GB"),
                properties -> {
            assertThat(properties.getProperty("san")).isEqualTo("Sanitised");
        });
    }

    @Test
    public void translateEnglishGBToUS() throws Exception {
        loadAndTest(new TestScenario("test_en_GB.properties")
                        .sourceLanguage("en")
                        .sourceCountry("GB")
                        .targetLanguage("en_US"),
                properties -> {
            assertThat(properties.getProperty("auth")).isEqualTo("authorized");
        });
    }

    @Test
    public void translateEnglishToGerman() throws Exception {
        loadAndTest(new TestScenario("test_en.properties")
                        .sourceLanguage("en")
                        .targetLanguage("de"),
                properties -> {
            assertThat(properties.getProperty("welcome")).isEqualTo("Willkommen im Dschungel.");
        });
    }

    @Test
    public void translateSpanishToUsEnglish() throws Exception {
        loadAndTest(new TestScenario("test_es.properties")
                        .sourceLanguage("es")
                        .targetLanguage("en_US"),
                properties -> {
                    assertThat(properties.getProperty("hello")).isEqualTo("Hello, sir");
                });
    }



    private class TestScenario {
        private final String propertyFile;
        private String sourceLanguage;
        private String sourceCountry;
        private String targetLanguage;

        private TestScenario(String propertyFile) {
            this.propertyFile = propertyFile;
        }

        public TestScenario sourceLanguage(String sourceLanguage) {
            this.sourceLanguage = sourceLanguage;
            return this;
        }
        public TestScenario sourceCountry(String sourceCountry) {
            this.sourceCountry = sourceCountry;
            return this;
        }
        public TestScenario targetLanguage(String targetLanguage) {
            this.targetLanguage = targetLanguage;
            return this;
        }


    }
}
