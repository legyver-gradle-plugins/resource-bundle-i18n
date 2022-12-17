package com.legyver.gradle.resourcebundlei18n.client.api;

public interface ArgumentOperator {

    String getTranslatableText(String targetLanguage);

    String restoreArgsInTranslatedText(String translatedText);

}
