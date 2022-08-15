package com.legyver.gradle.resourcebundlei18n;

import groovy.lang.Closure;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class ResourceBundleI18nPlugin implements Plugin<Project> {
    private static final Logger logger = Logging.getLogger(ResourceBundleI18nPlugin.class);

    @Override
    public void apply(Project project) {
        ResourceBundleI18nExtension extension = project.getExtensions()
                .create("resourceBundleI18n", ResourceBundleI18nExtension.class);
        project.task("i18n").doLast(new Closure(null) {
            @Override
            public Object call(Object... args) {
                generateResourceBundle(project, extension);
                return null;
            }
        });
    }

    private void generateResourceBundle(Project project, ResourceBundleI18nExtension extension) {
        String bundleName = extension.getBundleName().get();
        logger.debug("Found bundle name: {}", bundleName);
        if (bundleName == null) {
            throw new RuntimeException("Bundle name is required");
        }

        String[] targetLanguages = extension.getTargetLanguages().get();
        if (targetLanguages == null || targetLanguages.length == 0) {
            throw new RuntimeException("Target Languages are required");
        }
        logger.debug("Found target languages: {}", bundleName);

        String translationUrl = extension.getTranslationUrl().get();
        logger.debug("Found translation url: {}", translationUrl);
        if (translationUrl == null) {
            throw new RuntimeException("Translation URL is required");
        }

        String clientType = extension.getClient().get();
        logger.debug("Found client: {}", clientType);
        TranslationClientType translationClientType;
        if (clientType == null) {
            translationClientType = TranslationClientType.LIBRETRANSLATE;
        } else {
            translationClientType = TranslationClientType.valueOf(clientType);
        }


    }
}
