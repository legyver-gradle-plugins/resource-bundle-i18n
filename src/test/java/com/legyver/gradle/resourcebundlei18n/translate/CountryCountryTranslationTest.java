package com.legyver.gradle.resourcebundlei18n.translate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryCountryTranslationTest {

    @Test
    public void usToGB() {
        CountryCountryTranslation countryCountryTranslation = new CountryCountryTranslation("en", "US", "GB");
        assertThat(countryCountryTranslation.translate("authorized")).isEqualTo("authorised");
    }

    @Test
    public void gbToUS() {
        CountryCountryTranslation countryCountryTranslation = new CountryCountryTranslation("en", "GB", "US");
        assertThat(countryCountryTranslation.translate("authorised")).isEqualTo("authorized");
    }

    @Test
    public void usToNA() {
        CountryCountryTranslation countryCountryTranslation = new CountryCountryTranslation("en", "US", "NA");
        assertThat(countryCountryTranslation.translate("authorized")).isEqualTo("authorized");
    }

    @Test
    public void replaceWordWithPunctuation() {
        CountryCountryTranslation countryCountryTranslation = new CountryCountryTranslation("en", "US", "GB");
        assertThat(countryCountryTranslation.translate("You are not authorized.")).isEqualTo("You are not authorised.");
        assertThat(countryCountryTranslation.translate("You are not authorized...")).isEqualTo("You are not authorised...");
    }

    @Test
    public void replaceWordWithoutPunctuation() {
        CountryCountryTranslation countryCountryTranslation = new CountryCountryTranslation("en", "US", "GB");
        assertThat(countryCountryTranslation.translate("You are not authorized to be here")).isEqualTo("You are not authorised to be here");
    }

    @Test
    public void capitalizedWord() {
        CountryCountryTranslation countryCountryTranslation = new CountryCountryTranslation("en", "US", "GB");
        assertThat(countryCountryTranslation.translate("Authorization is required.")).isEqualTo("Authorisation is required.");
    }

}
