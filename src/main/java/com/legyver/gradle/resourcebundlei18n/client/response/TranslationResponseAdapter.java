package com.legyver.gradle.resourcebundlei18n.client.response;

public interface TranslationResponseAdapter<T> extends ResponseAdapter<T> {
    String getTranslation(String responseAsString);
}
