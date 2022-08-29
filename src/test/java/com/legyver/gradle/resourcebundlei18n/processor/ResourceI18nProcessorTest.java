package com.legyver.gradle.resourcebundlei18n.processor;

import com.legyver.gradle.resourcebundlei18n.TranslationClientType;
import com.legyver.gradle.resourcebundlei18n.client.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceI18nProcessorTest {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void initProcessorWin() throws Exception {
        String bundleName = "com.legyver.fenxlib.samples.about";

        String dirName = "out-test";
        File file = new File(dirName);
        String absolutePath = file.getAbsolutePath();
        String expectedBundleRelativePathWindows = "com\\legyver\\fenxlib\\samples";
        String expectedAbsoluteBundlePathWindows = absolutePath + "\\" + expectedBundleRelativePathWindows;
        Client client = new Client(new URL("http://localhost:5000"), TranslationClientType.LIBRETRANSLATE.getTranslationApi(null));
        ResourceI18nProcessor resourceI18nProcessor = new ResourceI18nProcessor(client, bundleName, new File(dirName));
        assertThat(resourceI18nProcessor.toString()).isEqualTo("ResourceI18nProcessor{" +
                "bundleName='" + bundleName + '\'' +
                ", bundleRelativePath='" + expectedBundleRelativePathWindows + '\'' +
                ", bundleAbsolutePath='" + expectedAbsoluteBundlePathWindows + '\'' +
                ", bundleShortName='" + "about" + '\'' +
                ", destinationDir=" + dirName +
                '}');
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void initProcessorLinux() throws Exception {
        String bundleName = "com.legyver.fenxlib.samples.about";

        String dirName = "out-test";
        File file = new File(dirName);
        String absolutePath = file.getAbsolutePath();
        String expectedBundleRelativePathWindows = "com/legyver/fenxlib/samples";
        String expectedAbsoluteBundlePathWindows = absolutePath + "/" + expectedBundleRelativePathWindows;
        Client client = new Client(new URL("http://localhost:5000"), TranslationClientType.LIBRETRANSLATE.getTranslationApi(null));
        ResourceI18nProcessor resourceI18nProcessor = new ResourceI18nProcessor(client, bundleName, new File(dirName));
        assertThat(resourceI18nProcessor.toString()).isEqualTo("ResourceI18nProcessor{" +
                "bundleName='" + bundleName + '\'' +
                ", bundleRelativePath='" + expectedBundleRelativePathWindows + '\'' +
                ", bundleAbsolutePath='" + expectedAbsoluteBundlePathWindows + '\'' +
                ", bundleShortName='" + "about" + '\'' +
                ", destinationDir=" + dirName +
                '}');
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void processProperties() throws Exception {
        String bundleName = "com.legyver.gradle.resourcebundlei18n.translate.test";
        File resourcesDir = new File("src\\test\\resources");
        Client client = new Client(new URL("http://localhost:5000"), TranslationClientType.LIBRETRANSLATE.getTranslationApi(null));
        ResourceI18nProcessor resourceI18nProcessor = new ResourceI18nProcessor(client, bundleName, resourcesDir);
        Set<File> resourceSet = new HashSet<>();
        resourceSet.add(resourcesDir);
        resourceI18nProcessor.processFiles(resourceSet, Arrays.asList("en_CA"));
        File result = new File("src\\test\\resources\\com\\legyver\\gradle\\resourcebundlei18n\\translate\\test_en_CA.properties");
        assertThat(result.exists()).isEqualTo(true);
    }
}
