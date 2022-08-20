package com.legyver.gradle.resourcebundlei18n.client.response.decorator;

import com.legyver.core.exception.CoreException;
import com.legyver.gradle.resourcebundlei18n.client.response.ResponseAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.TranslationResponseAdapter;

import java.util.Map;

public class TranslationMapDecorator extends  AbstractResponseAdapterDecorator<Map> implements TranslationResponseAdapter<Map> {

    public TranslationMapDecorator(ResponseAdapter<Map> responseAdapter) {
        super(responseAdapter);
    }

    @Override
    public String getTranslation(String responseAsString) throws CoreException {
        Map<String, Object> adaptedMap = adapt(responseAsString);
        Object value = adaptedMap.get("translatedText");
        if (value != null) {
            value = value.toString();
        }
        return (String) value;
    }
}
