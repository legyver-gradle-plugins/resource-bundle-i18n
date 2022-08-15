package com.legyver.gradle.resourcebundlei18n.client.api;

import com.legyver.gradle.resourcebundlei18n.client.request.RequestAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.LanguageIdentificationResponseAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.TranslationResponseAdapter;

public abstract class AbstractTranslationApi implements TranslationApi {

    private final RequestAdapter languageIdentificationRequestAdapter;
    private final LanguageIdentificationResponseAdapter languageIdentificationResponseAdapter;
    private final RequestAdapter translationRequestAdapter;
    private final TranslationResponseAdapter translationResponseAdapter;
    private ApiStrategy apiStrategy;

    public AbstractTranslationApi(RequestAdapter languageIdentificationRequestAdapter,
                                  LanguageIdentificationResponseAdapter languageIdentificationResponseAdapter,
                                  RequestAdapter translationRequestAdapter,
                                  TranslationResponseAdapter translationResponseAdapter) {
        this.languageIdentificationRequestAdapter = languageIdentificationRequestAdapter;
        this.languageIdentificationResponseAdapter = languageIdentificationResponseAdapter;
        this.translationRequestAdapter = translationRequestAdapter;
        this.translationResponseAdapter = translationResponseAdapter;
    }

    @Override
    public void setStrategy(ApiStrategy apiStrategy) {
        this.apiStrategy = apiStrategy;
    }

    @Override
    public String getAcceptedType() {
        return apiStrategy.getAcceptedType(this);
    }

    @Override
    public String getContentType() {
        return apiStrategy.getContentType(this);
    }

    @Override
    public String makeRequestBody(Object... values) {
        return apiStrategy.makeRequestBody(this, values);
    }

    @Override
    public String getResult(String responseAsString) {
        return apiStrategy.getResult(this, responseAsString);
    }

    @Override
    public String getEndpoint() {
        return apiStrategy.getEndpoint(this);
    }

    protected String getAcceptedTypeForDetectionRequest() {
        return languageIdentificationResponseAdapter.getAcceptedType();
    }
    protected String getContentTypeForDetectionRequest() {
        return languageIdentificationRequestAdapter.getContentType();
    }
    protected String makeRequestBodyContentForDetectionRequest(Object... values) {
        return languageIdentificationRequestAdapter.getContentAsString(values);
    }
    protected String getLanguageFromDetectionResponse(String responseAsString) {
        return languageIdentificationResponseAdapter.getIdentifiedLanguage(responseAsString);
    }

    protected String getAcceptedTypeForTranslationRequest() {
        return translationResponseAdapter.getAcceptedType();
    }
    protected String getContentTypeForTranslationRequest() {
        return translationRequestAdapter.getContentType();
    }
    protected String makeRequestBodyContentForTranslationRequest(Object[] values) {
        return translationRequestAdapter.getContentAsString(values);
    }
    protected String getTranslationFromTranslationResponse(String responseAsString) {
        return translationResponseAdapter.getTranslation(responseAsString);
    }

    protected abstract String getEndpointForDetectionRequest();
    protected abstract String getEndpointForTranslationRequest();

}
