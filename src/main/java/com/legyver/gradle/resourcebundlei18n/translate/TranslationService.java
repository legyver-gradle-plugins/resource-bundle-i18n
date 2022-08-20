package com.legyver.gradle.resourcebundlei18n.translate;

import com.legyver.core.exception.CoreException;
import com.legyver.gradle.resourcebundlei18n.client.Client;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.*;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;

public class TranslationService {
    private static final Logger logger = Logging.getLogger(TranslationService.class);
    private final DefaultCountryForLanguage defaultCountryForLanguage = new DefaultCountryForLanguage();
    private Client client;
    private Map<String, Map<String, CountrySpecificTranslation>> priorTranslations = new HashMap<>();

    public void setClient(Client client) {
        this.client = client;
    }

    public void translate(File file, String...targetLanguages) {
        String inputFileName = file.getName();
        if (!inputFileName.endsWith(".properties")) {
            throw new RuntimeException("Only .properties files are supported at this time");
        }
        if (targetLanguages == null || targetLanguages.length == 0) {
            throw new RuntimeException("Target languages are required");
        }

        Properties inputProperties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            inputProperties.load(fileInputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error("Couldn't find file", fileNotFoundException);
            throw new RuntimeException(fileNotFoundException);
        } catch (IOException ioException) {
            logger.error("Error reading file", ioException);
            throw new RuntimeException(ioException);
        }


        int lastIndexOfDot = inputFileName.lastIndexOf('.');
        String outputFilenameBase = inputFileName.substring(0, lastIndexOfDot - 1);
        String[] parts = outputFilenameBase.split("_");
        String sourceLanguage = null;
        String sourceCountry = null;
        if (parts.length > 1) {
            sourceLanguage = parts[1];
            if (parts.length == 3) {
                sourceCountry = parts[2];
            }
        }

        if (sourceLanguage == null) {
            try {
                sourceLanguage = detectLanguage(inputProperties);
            } catch (IOException | URISyntaxException ioException) {
                logger.error("Error detecting language", ioException);
                throw new RuntimeException(ioException);
            } catch (CoreException coreException) {
                logger.error("Error processing response while detecting language", coreException);
                throw new RuntimeException(coreException);
            }
        }

        for (String targetLanguage : targetLanguages) {
            File parentDir = file.getParentFile();
            parentDir.toPath().resolve(outputFilenameBase + "_" + targetLanguage + ".properties");
            File resultFile = new File(outputFilenameBase + "_" + targetLanguage + ".properties");
            try (FileOutputStream fileOutputStream = new FileOutputStream(resultFile)) {
                Properties resultProperties = translateProperties(inputProperties, sourceLanguage, sourceCountry, targetLanguage);
                resultProperties.store(fileOutputStream, "Generated by com.legyver.resource-bundle-i18n on " + ZonedDateTime.now());
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

    private String detectLanguage(Properties inputProperties) throws IOException, URISyntaxException, CoreException {
        //clump all the text together.
        StringJoiner stringJoiner = new StringJoiner(". ");
        for (String key : inputProperties.stringPropertyNames()) {
            stringJoiner.add(inputProperties.getProperty(key));
        }
        String text = stringJoiner.toString();
        return client.detectLanguage(text);
    }

    protected Properties translateProperties(Properties sourceProperties, String sourceLanguage, String sourceCountry, String targetLanguage) throws IOException, CoreException {
        Properties properties = new Properties();

        String[] parts = targetLanguage.split("_");
        String tLanguage = parts[0];//drop the country
        String tCountry = null;
        if (parts.length > 1) {
            tCountry = parts[1];
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
            String translatedToCountry = null;
            Map<String, CountrySpecificTranslation> translationsByLanguage = this.priorTranslations.computeIfAbsent(translateMe, x -> new HashMap<>());
            if (translateType == TranslateType.LANGUAGE) {
                if (translationsByLanguage.containsKey(tLanguage)) {
                    CountrySpecificTranslation countrySpecificTranslation = translationsByLanguage.get(tLanguage);
                    translation = countrySpecificTranslation.translatedText;
                    translatedToLanguage = tLanguage;
                    translatedToCountry = countrySpecificTranslation.country;
                } else {
                    translation = client.getTranslation(translateMe, sourceLanguage, tLanguage);
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