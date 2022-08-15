package com.legyver.gradle.resourcebundlei18n.translate;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountryCountryTranslation {
    private static final Logger logger = Logging.getLogger(CountryCountryTranslation.class);
    private static final Pattern lettersOnly = Pattern.compile("(([\\w])+)(([\\W])*)");

    private final String language;
    private final String sourceCountry;
    private final String targetCountry;
    private Properties properties;

    public CountryCountryTranslation(String language, String sourceCountry, String targetCountry) {
        this.language = language;
        this.sourceCountry = sourceCountry;
        this.targetCountry = targetCountry;
    }

    public String translate(String original) {
        boolean loaded = true;
        if (properties == null) {
            loaded = false;
            properties = new Properties();
            String spellingFileName = "spellingdifferences_" + language + "_" + sourceCountry + "_" + targetCountry + ".properties";
            try (InputStream inputStream = CountryCountryTranslation.class.getResourceAsStream(spellingFileName)) {
                properties.load(inputStream);
                loaded = true;
            } catch (IOException|RuntimeException ioException) {
                logger.debug("No resource found by {}", spellingFileName);
                String spellingFileName2 = "spellingdifferences_" + language + "_" + targetCountry + "_" + sourceCountry + ".properties";
                try (InputStream inputStream = CountryCountryTranslation.class.getResourceAsStream(spellingFileName2)) {
                    properties.load(inputStream);
                    Properties tProperties = new Properties();
                    for (String propertyKey : properties.stringPropertyNames()) {
                        String value = properties.getProperty(propertyKey);
                        tProperties.put(value, propertyKey);
                    }
                    properties = tProperties;
                    loaded = true;
                } catch (IOException|RuntimeException ioExceptionReversed) {
                    logger.warn("Unable to correct for country-specific spelling.  No resource found by {} or {}.", spellingFileName, spellingFileName2);
                }
            }
        }
        String result = original;
        if (loaded) {
            Scanner scanner = new Scanner(original);
            StringJoiner wordJoiner = new StringJoiner(" ");
            while (scanner.hasNext()) {
                String originalWord = scanner.next();
                String word = originalWord;
                String suffix = null;
                Matcher wordMatcher = lettersOnly.matcher(originalWord);
                 if (wordMatcher.find()) {
                     word = wordMatcher.group(1);
                     suffix = wordMatcher.group(3);
                 }
                 if (suffix == null) {
                     suffix = "";
                 }

                String wordLC = word.toLowerCase();
                if (properties.containsKey(wordLC)) {
                    String replacement = properties.getProperty(wordLC);
                    int char0Original = word.charAt(0);
                    int char0Replacement = replacement.charAt(0);
                    if (char0Original < 97) {
                        char start = (char) (char0Replacement - 32);
                        word = start + replacement.substring(1);
                    } else {
                        word = replacement;
                    }
                }
                wordJoiner.add(word + suffix);
            }
            result = wordJoiner.toString();
        }
        return result;
    }
}
