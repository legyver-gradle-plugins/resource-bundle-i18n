package com.legyver.gradle.resourcebundlei18n.client.response.decorator;

import com.legyver.gradle.resourcebundlei18n.client.response.ResponseAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.TranslationResponseAdapter;

import java.util.Map;

public class TranslationMapDecorator extends  AbstractResponseAdapterDecorator<Map> implements TranslationResponseAdapter<Map> {

    public TranslationMapDecorator(ResponseAdapter<Map> responseAdapter) {
        super(responseAdapter);
    }

    @Override
    public String getTranslation(String responseAsString) {
        Map<String, String> adaptedMap = adapt(responseAsString);
        return adaptedMap.get("translatedText");
    }
}
