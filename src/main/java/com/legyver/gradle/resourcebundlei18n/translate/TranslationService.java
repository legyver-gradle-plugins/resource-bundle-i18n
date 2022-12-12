package com.legyver.gradle.resourcebundlei18n.translate;

import com.legyver.core.exception.CoreException;
import com.legyver.gradle.resourcebundlei18n.client.Client;
import com.legyver.utils.propl.PropertyList;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslationService {
    private static final Logger logger = Logging.getLogger(TranslationService.class);
    private final DefaultCountryForLanguage defaultCountryForLanguage = new DefaultCountryForLanguage();
    private Client client;
    private Map<String, Map<String, CountrySpecificTranslation>> priorTranslations = new HashMap<>();

    public void setClient(Client client) {
        this.client = client;
    }

    public void translate(File file, String...targetLanguages) {
        logger.info("Translating file {} to languages {}", file.getName(), targetLanguages);
        String inputFileName = file.getName();
        if (!inputFileName.endsWith(".properties")) {
            logger.error("Only .properties files are supported at this time");
            throw new RuntimeException("Only .properties files are supported at this time");
        }
        if (targetLanguages == null || targetLanguages.length == 0) {
            logger.error("Target languages are required");
            throw new RuntimeException("Target languages are required");
        }

        PropertyList inputProperties = new PropertyList();
        try {
            inputProperties.load(file);
            logger.info("loaded properties file {}", file.getName());
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error("Couldn't find file", fileNotFoundException);
            throw new RuntimeException(fileNotFoundException);
        }

        int lastIndexOfDot = inputFileName.lastIndexOf('.');
        String outputFilenameBase = inputFileName.substring(0, lastIndexOfDot);
        logger.debug("OutputFilenameBase: {}", outputFilenameBase);
        String[] parts = outputFilenameBase.split("_");

        if (logger.isDebugEnabled()) {
            logger.debug("parts: [{}]", Stream.of(parts).collect(Collectors.joining(", ")));
        }
        String sourceLanguage = null;
        String sourceCountry = null;
        if (parts.length > 1) {
            outputFilenameBase = parts[0];
            sourceLanguage = parts[1];
            if (parts.length == 3) {
                sourceCountry = parts[2];
            }
        }

        if (sourceLanguage == null) {
            try {
                logger.info("Detecting language.");
                sourceLanguage = detectLanguage(inputProperties);
                logger.info("Language detected: {}", sourceLanguage);
            } catch (IOException | URISyntaxException ioException) {
                logger.error("Error detecting language", ioException);
                throw new RuntimeException(ioException);
            } catch (CoreException coreException) {
                logger.error("Error processing response while detecting language", coreException);
                throw new RuntimeException(coreException);
            }
        }
        logger.info("Source language: {}", sourceLanguage);

        if (sourceCountry == null) {
            sourceCountry = defaultCountryForLanguage.getDefaultCountryForLanguage(sourceLanguage);
            logger.info("Country for language [{}] defaulted to: {}", sourceLanguage, sourceCountry);
        }
        logger.info("Source country: {}", sourceCountry);

        for (String targetLanguage : targetLanguages) {
            logger.info("Processing for target language: {}", targetLanguage);
            parts = targetLanguage.split("_");
            if (parts.length > 1) {
                targetLanguage = parts[0];
                for (int i = 1; i < parts.length; i++) {
                    targetLanguage += "_" + parts[i].toUpperCase();
                }
            }
            File parentDir = file.getParentFile();
            Path newFile = parentDir.toPath().resolve(outputFilenameBase + "_" + targetLanguage + ".properties");
            File resultFile = newFile.toFile();
            logger.debug("Result file: {}", resultFile);
            try {
                PropertyList resultProperties = translateProperties(inputProperties, sourceLanguage, sourceCountry, targetLanguage);
                resultProperties.write(resultFile);
            } catch (FileNotFoundException fileNotFoundException) {
                logger.error("Couldn't find file", fileNotFoundException);
                throw new RuntimeException(fileNotFoundException);
            } catch (IOException ioException) {
                logger.error("Error doing translation", ioException);
                throw new RuntimeException(ioException);
            } catch (CoreException coreException) {
                logger.error("Error reading response");
                throw new RuntimeException(coreException);
            }
        }
    }

    private String detectLanguage(PropertyList inputProperties) throws IOException, URISyntaxException, CoreException {
        //clump all the text together.
        StringJoiner stringJoiner = new StringJoiner(". ");
        for (String key : inputProperties.stringPropertyNames()) {
            stringJoiner.add(inputProperties.getProperty(key));
        }
        String text = stringJoiner.toString();
        logger.debug("App properties: [{}]", text);
        return client.detectLanguage(text);
    }

    protected PropertyList translateProperties(PropertyList sourceProperties, String sourceLanguage, String sourceCountry, String targetLanguage) throws IOException, CoreException {
        PropertyList properties = new PropertyList();

        String[] parts = targetLanguage.split("_");
        String tLanguage = parts[0];//drop the country
        String tCountry = null;
        if (parts.length > 1) {
            tCountry = parts[1];
        }
        if (tCountry == null) {
            tCountry = defaultCountryForLanguage.getDefaultCountryForLanguage(tLanguage);
            logger.debug("Defaulting target country for language [{}]  to: {}", tLanguage, tCountry);
        }

        TranslateType translateType = TranslateType.LANGUAGE;
        if (Objects.equals(sourceLanguage, tLanguage)) {
            if (Objects.equals(sourceCountry, tCountry)) {
                logger.info("No translation to be done from {}_{} to {}", sourceLanguage, sourceCountry, targetLanguage);
                return sourceProperties;
            }
            translateType = TranslateType.COUNTRY;
        }
        CountryCountryTranslation postTranslationCountryAdjustment = new CountryCountryTranslation(sourceLanguage, sourceCountry, tCountry);


        for (String key : sourceProperties.stringPropertyNames()) {
            String translateMe = sourceProperties.getProperty(key);
            String translation = translateMe;
            String translatedToLanguage = null;
            String translatedToCountry = sourceCountry;
            Map<String, CountrySpecificTranslation> translationsByLanguage = this.priorTranslations.computeIfAbsent(translateMe, x -> new HashMap<>());
            if (translateType == TranslateType.LANGUAGE) {
                if (translationsByLanguage.containsKey(tLanguage)) {
                    CountrySpecificTranslation countrySpecificTranslation = translationsByLanguage.get(tLanguage);
                    translation = countrySpecificTranslation.translatedText;
                    translatedToLanguage = tLanguage;
                    translatedToCountry = countrySpecificTranslation.country;
                } else {
                    translation = client.getTranslation(translateMe, sourceLanguage, tLanguage);
                    logger.debug("Translated '{}' to '{}'", translateMe, translation);
                    translatedToLanguage = tLanguage;
                    translatedToCountry = defaultCountryForLanguage.getDefaultCountryForLanguage(tLanguage);
                    CountrySpecificTranslation countryCountryTranslation = new CountrySpecificTranslation(translation, translatedToCountry);
                    translationsByLanguage.put(translatedToLanguage, countryCountryTranslation);
                }
            }
            if (translateType == TranslateType.COUNTRY) {
                //this case is when no translation has occurred, ie: because the source and target languages are the same and the countries different
                translation = postTranslationCountryAdjustment.translate(translation);
            } else if (translatedToCountry != null && !translatedToCountry.equals(tCountry)) {
                //this has been translated from the source language to the target language but not including country variances
                translation = new CountryCountryTranslation(tLanguage, translatedToCountry, tCountry).translate(translation);
            }
            translation = ArgumentSanitizer.sanitize(translation);
            properties.put(key, translation);
        }
        return properties;
    }

    enum TranslateType {
        COUNTRY,
        LANGUAGE
    }

    private static class CountrySpecificTranslation {
        private final String translatedText;
        private final String country;

        private CountrySpecificTranslation(String translatedText, String country) {
            this.translatedText = translatedText;
            this.country = country;
        }
    }
}
