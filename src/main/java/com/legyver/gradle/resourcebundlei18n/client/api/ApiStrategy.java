package com.legyver.gradle.resourcebundlei18n.client.api;

public interface ApiStrategy {

    String getAcceptedType(AbstractTranslationApi abstractTranslationApi);

    String getContentType(AbstractTranslationApi abstractTranslationApi);

    String makeRequestBody(AbstractTranslationApi abstractTranslationApi, Object[] values);

    String getResult(AbstractTranslationApi abstractTranslationApi, String responseAsString);

    String getEndpoint(AbstractTranslationApi abstractTranslationApi);
}
