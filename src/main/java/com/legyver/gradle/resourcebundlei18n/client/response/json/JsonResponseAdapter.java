package com.legyver.gradle.resourcebundlei18n.client.response.json;

import com.legyver.gradle.resourcebundlei18n.client.response.ResponseAdapter;

/**
 * Adapter interface for a JSON response
 */
public interface JsonResponseAdapter<T> extends ResponseAdapter<T> {
    @Override
    default String getAcceptedType() {
        return "application/json";
    }
}
