package com.legyver.gradle.resourcebundlei18n.client.api;

import com.legyver.core.exception.CoreException;

public class TranslationApiStrategy implements ApiStrategy {
    @Override
    public String getAcceptedType(AbstractTranslationApi abstractTranslationApi) {
        return abstractTranslationApi.getAcceptedTypeForTranslationRequest();
    }

    @Override
    public String getContentType(AbstractTranslationApi abstractTranslationApi) {
        return abstractTranslationApi.getContentTypeForTranslationRequest();
    }

    @Override
    public String makeRequestBody(AbstractTranslationApi abstractTranslationApi, Object[] values) {
        return abstractTranslationApi.makeRequestBodyContentForTranslationRequest(values);
    }

    @Override
    public String getResult(AbstractTranslationApi abstractTranslationApi, String responseAsString) throws CoreException {
        return abstractTranslationApi.getTranslationFromTranslationResponse(responseAsString);
    }

    @Override
    public String getEndpoint(AbstractTranslationApi abstractTranslationApi) {
        return abstractTranslationApi.getEndpointForTranslationRequest();
    }
}
