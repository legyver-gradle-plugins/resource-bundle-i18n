package com.legyver.gradle.resourcebundlei18n;

import org.gradle.api.provider.Property;


/**
 * Supported configurations
 * <code>
 *     resourceBundleI18n {
 *         translationUrl = "http://localhost:5000"
 *         targetLanguages = ["en", "es", "de", "fr"]
 *         bundleName = "com.example.bundle" //this will match src/main/resources/com/example/bundle.properties as the source
 *         client = "LIBRETRANSLATE" //the client to use to communicate with the translationUrl
 *         //("LIBRETRANSLATE" is also the default value as it is the only client supported at this time)
 *     }
 * </code>
 */
public interface ResourceBundleI18nExtension {
    Property<String> getTranslationUrl();
    Property<String[]> getTargetLanguages();
    Property<String> getBundleName();
    Property<String> getClient();
}
