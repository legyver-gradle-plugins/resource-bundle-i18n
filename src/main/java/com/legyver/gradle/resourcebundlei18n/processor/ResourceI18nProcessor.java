package com.legyver.gradle.resourcebundlei18n.processor;

import com.legyver.gradle.resourcebundlei18n.client.Client;
import com.legyver.gradle.resourcebundlei18n.translate.TranslationService;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ResourceI18nProcessor {
    private static final Logger logger = Logging.getLogger(ResourceI18nProcessor.class);

    private final TranslationService translationService = new TranslationService();
    private final String bundleName;
    private final String bundleRelativePath;
    private final String bundleAbsolutePath;
    private final String bundleShortName;
    private final File destinationDir;
    private boolean sourcePropertiesLoaded;

    public ResourceI18nProcessor(Client client, String bundleName, File destinationDir) {
        translationService.setClient(client);
        this.bundleName = bundleName;
        this.destinationDir = destinationDir;
        int lastDot = bundleName.lastIndexOf('.');
        String showWork = bundleName.substring(0, lastDot);
        String fileSeparator = File.separator;
        if ("\\".equals(fileSeparator)) {
            fileSeparator = "\\\\";
        }
        this.bundleRelativePath = showWork.replaceAll("(\\.)+", fileSeparator);
        this.bundleShortName = bundleName.substring(lastDot + 1);

        String buildResourcesMain;
        String srcMainResources;
        String buildResourcesTest;
        String srcTestResources;
        if (File.separator.equals("\\")) {
            buildResourcesMain = "\\\\build\\\\resources\\\\main";
            srcMainResources = "\\\\src\\\\main\\\\resources";
            buildResourcesTest = "\\\\build\\\\resources\\\\test";
            srcTestResources = "\\\\src\\\\test\\\\resources";
        } else {
            buildResourcesMain = "/build/resources/main";
            srcMainResources = "/src/main/resources";
            buildResourcesTest = "/build/resources/test";
            srcTestResources = "/src/test/resources";
        }

        String matchDirectory = destinationDir.getAbsolutePath()
                .replaceAll(buildResourcesMain, srcMainResources)
                .replaceAll(buildResourcesTest, srcTestResources);

        this.bundleAbsolutePath = matchDirectory + File.separator + bundleRelativePath;
        logger.info("Configured processor: {}", this);
    }

    public void processFiles(Set<File> files, List<String> targetLanguages) {
        for (File file: files) {
            if (file.isDirectory()) {
                logger.trace("Traversing directory: {}", file.getName());
                processFiles(file.listFiles(), targetLanguages);
            } else {
                processFile(file, targetLanguages);
            }
        }
    }

    private void processFiles(File[] files, List<String> targetLanguages) {
        if (!sourcePropertiesLoaded) {
            for (File file : files) {
                if (file.isDirectory()) {
                    logger.trace("Traversing directory: {}", file.getName());
                    processFiles(file.listFiles(), targetLanguages);
                } else if (!sourcePropertiesLoaded) {
                    processFile(file, targetLanguages);
                }
            }
        }
    }

    private void processFile(File file, List<String> targetLanguages) {
        String fileName = file.getName();
        String nameMinusExtension = nameMinusExtension(fileName);
        File parent = file.getParentFile();
        String parentPath = parent.getAbsolutePath();
        if (!sourcePropertiesLoaded && parentPath.equals(bundleAbsolutePath) && nameMinusExtension.startsWith(bundleShortName)) {
            logger.info("Processing file for translation: {}", file.getName());
            translationService.translate(file, targetLanguages.toArray(new String[targetLanguages.size()]));
            sourcePropertiesLoaded = true;
        } else {
            logger.debug("Skipping parentPath [{}] file: {}", parentPath, file.getName());
        }
    }

    private String nameMinusExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        String result = filename.substring(0, lastDot);
        return result;
    }


    @Override
    public String toString() {
        return "ResourceI18nProcessor{" +
                "bundleName='" + bundleName + '\'' +
                ", bundleRelativePath='" + bundleRelativePath + '\'' +
                ", bundleAbsolutePath='" + bundleAbsolutePath + '\'' +
                ", bundleShortName='" + bundleShortName + '\'' +
                ", destinationDir=" + destinationDir +
                '}';
    }
}
