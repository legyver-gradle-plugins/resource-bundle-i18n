package com.legyver.gradle.resourcebundlei18n.client.api;

public interface TranslationApi {
    String makeRequestBody(Object...values);
    String getAcceptedType();
    String getContentType();

    void setStrategy(ApiStrategy apiStrategy);

    String getResult(String responseAsString);

    String getEndpoint();
}
