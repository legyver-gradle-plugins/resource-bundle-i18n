package com.legyver.gradle.resourcebundlei18n.translate;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentSanitizer {
    private static final Logger logger = Logging.getLogger(ArgumentSanitizer.class);
    public static String sanitize(String text) {
        Pattern pattern = Pattern.compile("(\\{(\\d)+.?\\})+");
        Matcher matcher = pattern.matcher(text);
        String result = text;

        while (matcher.find()) {
            String groupFull = matcher.group(1);
            String digit = matcher.group(2);
            String correct = "{" + digit + "}";
            logger.debug("Replacing {} with {}", groupFull, correct);
            result = result.replace(groupFull, correct);
        }
        return result;
    }
}
