package com.legyver.gradle.resourcebundlei18n.translate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCountryForLanguageTest {

    @Test
    public void spotCheck() {
        DefaultCountryForLanguage defaultCountryForLanguage = new DefaultCountryForLanguage();
        assertThat(defaultCountryForLanguage.getDefaultCountryForLanguage("en")).isEqualTo("US");
        assertThat(defaultCountryForLanguage.getDefaultCountryForLanguage("es")).isEqualTo("ES");
    }
}
