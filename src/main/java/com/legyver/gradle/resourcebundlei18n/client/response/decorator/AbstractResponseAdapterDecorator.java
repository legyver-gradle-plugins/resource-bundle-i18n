package com.legyver.gradle.resourcebundlei18n.client.response.decorator;

import com.legyver.gradle.resourcebundlei18n.client.response.ResponseAdapter;

public abstract class AbstractResponseAdapterDecorator<T> implements ResponseAdapter<T> {

    private final ResponseAdapter<T> responseAdapter;

    protected AbstractResponseAdapterDecorator(ResponseAdapter<T> responseAdapter) {
        this.responseAdapter = responseAdapter;
    }

    @Override
    public String getAcceptedType() {
        return responseAdapter.getAcceptedType();
    }

    @Override
    public T adapt(String responseAsString) {
        return responseAdapter.adapt(responseAsString);
    }
}
