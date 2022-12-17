package com.legyver.gradle.resourcebundlei18n.client.api.libretranslate;

import com.legyver.gradle.resourcebundlei18n.client.api.ArgumentOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Argument operator to workaround how LibreTranslate handles special characters when translating from to languages like spanish.
 * Example problem areas:
 *    'Hello, {0}' translated to Spanish ends up 'Hola.'
 *    'Hello, {0} my {1}!' translated to Spanish ends up '¡Hola, mi!'
 *
 * The general algorithm is to swap out the argument with a UUID (which we assume will not be translated), and then do the process
 * in reverse on the translation.
 */
public class LibreTranslateArgumentOperator implements ArgumentOperator {

    private static final Pattern argPattern = Pattern.compile("(\\{(\\d)+.?\\})+");
    private static final String untranslatablePlaceholder = UUID.randomUUID().toString();
    private final String textToTranslate;
    private final Matcher matcher;
    private final List<String> restorePoints = new ArrayList<>();

    public LibreTranslateArgumentOperator(String textToTranslate) {
        this.textToTranslate = textToTranslate;
        this.matcher = argPattern.matcher(textToTranslate);
    }

    @Override
    public String getTranslatableText(String targetLanguage) {
        String translatableText = textToTranslate;
        //bug seems localized to spanish
        if (targetLanguage.startsWith("es")) {
            //iterate over all matches and split into segments
            int start = 0;
            while (matcher.find()) {
                String arg = matcher.group(1);
                this.restorePoints.add(arg);
                translatableText = translatableText.replace(arg, untranslatablePlaceholder);
            }
        }
        return translatableText;
    }

    @Override
    public String restoreArgsInTranslatedText(String translatedText) {
        String result = translatedText;

        for (String restorePoint : restorePoints) {
            int indexFirst = result.indexOf(untranslatablePlaceholder);
            String pre = result.substring(0, indexFirst);
            String post = result.substring(indexFirst + untranslatablePlaceholder.length());
            result = pre + restorePoint + post;
        }
        return result;
    }

    protected String getUntranslatablePlaceholder() {
        return untranslatablePlaceholder;
    }
}
