package com.legyver.gradle.resourcebundlei18n.client.response;

import com.legyver.core.exception.CoreException;

public interface LanguageIdentificationResponseAdapter<T> extends ResponseAdapter<T> {
    String getIdentifiedLanguage(String responseAsString) throws CoreException;
}
