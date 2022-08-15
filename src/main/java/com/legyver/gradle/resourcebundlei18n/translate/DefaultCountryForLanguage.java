package com.legyver.gradle.resourcebundlei18n.translate;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DefaultCountryForLanguage {
    private static final Logger logger = Logging.getLogger(DefaultCountryForLanguage.class);

    private Properties properties;

    public String getDefaultCountryForLanguage(String languageCode) {
        if (properties == null) {
            properties = new Properties();
            try (InputStream inputStream = DefaultCountryForLanguage.class.getResourceAsStream("default-country-for-lang.properties")) {
                properties.load(inputStream);
            } catch (IOException ioException) {
                logger.error("Unable to load file {}", "default-country-for-lang.properties");
            }
        }
        return properties.getProperty(languageCode);
    }

}
