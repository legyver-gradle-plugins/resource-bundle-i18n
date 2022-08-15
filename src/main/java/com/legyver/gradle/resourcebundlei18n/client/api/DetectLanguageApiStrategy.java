package com.legyver.gradle.resourcebundlei18n.client.api;

public class DetectLanguageApiStrategy implements ApiStrategy {
    @Override
    public String getAcceptedType(AbstractTranslationApi abstractTranslationApi) {
        return abstractTranslationApi.getAcceptedTypeForDetectionRequest();
    }

    @Override
    public String getContentType(AbstractTranslationApi abstractTranslationApi) {
        return abstractTranslationApi.getContentTypeForDetectionRequest();
    }

    @Override
    public String makeRequestBody(AbstractTranslationApi abstractTranslationApi, Object... values) {
        return abstractTranslationApi.makeRequestBodyContentForDetectionRequest(values);
    }

    @Override
    public String getResult(AbstractTranslationApi abstractTranslationApi, String responseAsString) {
        return abstractTranslationApi.getLanguageFromDetectionResponse(responseAsString);
    }

    @Override
    public String getEndpoint(AbstractTranslationApi abstractTranslationApi) {
        return abstractTranslationApi.getEndpointForDetectionRequest();
    }
}
