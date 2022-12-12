package com.legyver.gradle.resourcebundlei18n;

import com.legyver.gradle.resourcebundlei18n.client.Client;
import com.legyver.gradle.resourcebundlei18n.processor.ResourceI18nProcessor;
import groovy.lang.Closure;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceBundleI18nPlugin implements Plugin<Project> {
    private static final Logger logger = Logging.getLogger(ResourceBundleI18nPlugin.class);

    @Override
    public void apply(Project project) {
        ResourceBundleI18nExtension extension = project.getExtensions()
                .create("resourceBundleI18n", ResourceBundleI18nExtension.class);
        project.task("i18n").doLast(new Closure(null) {
            @Override
            public Object call(Object... args) {
                translateResourceBundle(project, extension);
                return null;
            }
        });
    }

    private void translateResourceBundle(Project project, ResourceBundleI18nExtension extension) {
        String bundleName = extension.getBundleName().get();
        logger.debug("Found bundle name: {}", bundleName);
        if (bundleName == null) {
            throw new RuntimeException("Bundle name is required");
        }

        List<String> targetLanguages = extension.getTargetLanguages().get();
        if (targetLanguages == null || targetLanguages.size() == 0) {
            throw new RuntimeException("Target Languages are required");
        }
        if (logger.isDebugEnabled()) {
            String targetLanguageCSV = targetLanguages.stream().collect(Collectors.joining(", "));
            logger.debug("Found target languages: {}", targetLanguageCSV);
        }

        String sTranslationUrl = extension.getTranslationUrl().get();
        logger.debug("Found translation url: {}", sTranslationUrl);
        if (sTranslationUrl == null) {
            throw new RuntimeException("Translation URL is required");
        }
        URL url;
        try {
            url = new URL(sTranslationUrl);
        } catch (MalformedURLException e) {
            logger.error("Error deciphering URL: " + sTranslationUrl, e);
            throw new RuntimeException(e);
        }

        TranslationClientType translationClientType;
        if (extension.getClient().isPresent()) {
            String clientType = extension.getClient().get();
            logger.info("Found client: {}", clientType);
            translationClientType = TranslationClientType.valueOf(clientType);
        } else {
            translationClientType = TranslationClientType.LIBRETRANSLATE;
            logger.info("Using default client: {}", translationClientType);
        }

        String apiKey = null;
        if (extension.getClientApiKey().isPresent()) {
            apiKey = extension.getClientApiKey().get();
            logger.debug("found API key: {}", apiKey);
        }
        Client client = new Client(url, translationClientType.getTranslationApi(apiKey));

        JavaPluginExtension javaPluginExtension = project.getExtensions().findByType(JavaPluginExtension.class);

        SourceSet sourceSet = javaPluginExtension.getSourceSets().findByName(SourceSet.MAIN_SOURCE_SET_NAME);
        SourceDirectorySet sourceDirectorySet = sourceSet.getResources();

        sourceDirectorySet.getFilter().include("**/*.properties");
        FileCollection sourceDirectories = sourceDirectorySet.getSourceDirectories();
        Set<File> files = sourceDirectories.getFiles();
        if (logger.isDebugEnabled()) {
            logRecursive(files);
        }

        ProcessResources processResources = (ProcessResources) project.getTasks().findByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME);
        File destinationDir = processResources.getDestinationDir();

        ResourceI18nProcessor resourceProcessor = new ResourceI18nProcessor(client, bundleName, destinationDir);
        resourceProcessor.processFiles(files, targetLanguages);
    }

    private void logRecursive(Collection<File> files) {
        if (files.isEmpty()) {
            logger.debug("No files found");
        } else {
            for (File file : files) {
                logRecursive(file);
            }
        }
    }

    private void logRecursive(File file) {
        logger.debug("File found: {}", file);
        if (file.isDirectory()) {
            logRecursive(file.listFiles());
        }
    }

    private void logRecursive(File[] listFiles) {
        logRecursive(Arrays.asList(listFiles));
    }
}
