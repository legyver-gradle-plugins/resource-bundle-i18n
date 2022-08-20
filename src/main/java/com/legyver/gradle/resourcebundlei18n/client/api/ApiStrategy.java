package com.legyver.gradle.resourcebundlei18n.client.api;

import com.legyver.core.exception.CoreException;

public interface ApiStrategy {

    String getAcceptedType(AbstractTranslationApi abstractTranslationApi);

    String getContentType(AbstractTranslationApi abstractTranslationApi);

    String makeRequestBody(AbstractTranslationApi abstractTranslationApi, Object[] values);

    String getResult(AbstractTranslationApi abstractTranslationApi, String responseAsString) throws CoreException;

    String getEndpoint(AbstractTranslationApi abstractTranslationApi);
}
