package com.legyver.gradle.resourcebundlei18n.client.response;

public interface LanguageIdentificationResponseAdapter<T> extends ResponseAdapter<T> {
    String getIdentifiedLanguage(String responseAsString);
}
