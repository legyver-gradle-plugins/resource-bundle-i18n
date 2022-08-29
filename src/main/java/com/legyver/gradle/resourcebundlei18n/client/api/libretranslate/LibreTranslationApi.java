package com.legyver.gradle.resourcebundlei18n.client.api.libretranslate;

import com.legyver.gradle.resourcebundlei18n.client.api.AbstractTranslationApi;
import com.legyver.gradle.resourcebundlei18n.client.request.json.MessageFormatJsonRequestAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.decorator.LanguageIdentificationMapDecorator;
import com.legyver.gradle.resourcebundlei18n.client.response.decorator.TranslationMapDecorator;
import com.legyver.gradle.resourcebundlei18n.client.response.json.FirstOfListMapAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.json.JsonResponseMapAdapter;
import com.legyver.gradle.resourcebundlei18n.client.response.json.ListOfMapAdapter;

public class LibreTranslationApi extends AbstractTranslationApi {
    public LibreTranslationApi(String apiKey) {
        super(new MessageFormatJsonRequestAdapter("\"q\": \"{0}\", \"api_key\": \"" + apiKey + "\""),
                new LanguageIdentificationMapDecorator(new FirstOfListMapAdapter(new ListOfMapAdapter())),
                new MessageFormatJsonRequestAdapter("\"q\": \"{0}\", \"source\": \"{1}\", \"target\": \"{2}\", \"format\": \"text\", \"api_key\": \"" +  apiKey + "\""),
                new TranslationMapDecorator(new JsonResponseMapAdapter()));
    }

    public LibreTranslationApi() {
        this("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
    }

    @Override
    protected String getEndpointForDetectionRequest() {
        return "/detect";
    }

    @Override
    protected String getEndpointForTranslationRequest() {
        return "/translate";
    }
}
