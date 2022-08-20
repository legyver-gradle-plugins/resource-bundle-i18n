package com.legyver.gradle.resourcebundlei18n.client.response.decorator;

import com.legyver.core.exception.CoreException;
import com.legyver.gradle.resourcebundlei18n.client.response.LanguageIdentificationResponseAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.json.JsonResponseMapAdapter;

import java.util.Map;

public class LanguageIdentificationMapDecorator extends AbstractResponseAdapterDecorator<Map> implements LanguageIdentificationResponseAdapter<Map> {

    public LanguageIdentificationMapDecorator(JsonResponseMapAdapter mapAdapter) {
        super(mapAdapter);
    }

    @Override
    public String getIdentifiedLanguage(String responseAsString) throws CoreException {
        Map<String, Object> adaptedMap = adapt(responseAsString);
        return (String) adaptedMap.get("language");
    }

}
