package com.legyver.gradle.resourcebundlei18n;

import com.legyver.gradle.resourcebundlei18n.client.api.TranslationApi;
import com.legyver.gradle.resourcebundlei18n.client.api.libretranslate.LibreTranslationApi;

public enum TranslationClientType {
    LIBRETRANSLATE {
        @Override
        public TranslationApi getTranslationApi(String apiKey) {
            return apiKey == null ?
                    new LibreTranslationApi() :
                    new LibreTranslationApi(apiKey);
        }
    };

    public abstract TranslationApi getTranslationApi(String apiKey);
}
