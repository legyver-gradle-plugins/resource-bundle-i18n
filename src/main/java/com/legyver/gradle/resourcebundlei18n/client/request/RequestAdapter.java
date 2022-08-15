package com.legyver.gradle.resourcebundlei18n.client.request;

public interface RequestAdapter<T> {
    String getContentType();
    String getContentAsString(T rawContent);
}
