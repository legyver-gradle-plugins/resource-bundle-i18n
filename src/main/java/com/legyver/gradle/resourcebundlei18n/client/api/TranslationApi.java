package com.legyver.gradle.resourcebundlei18n.client.api;

import com.legyver.core.exception.CoreException;

public interface TranslationApi {
    String makeRequestBody(Object...values);
    String getAcceptedType();
    String getContentType();

    void setStrategy(ApiStrategy apiStrategy);

    String getResult(String responseAsString) throws CoreException;

    String getEndpoint();

    ArgumentOperator getArgumentOperator(String textToTranslate);
}
