package com.legyver.gradle.resourcebundlei18n.client.response;

import com.legyver.core.exception.CoreException;

public interface TranslationResponseAdapter<T> extends ResponseAdapter<T> {
    String getTranslation(String responseAsString) throws CoreException;
}
