package com.legyver.gradle.resourcebundlei18n.translate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArgumentSanitizerTest {

    @Test
    public void cleanupSingleArgs() {
        String english = "Hello {0}!";
        String german = "Hallo {0!}!";

        String sanitizedEnglish = ArgumentSanitizer.sanitize(english);
        assertThat(sanitizedEnglish).isEqualTo(english);
        String sanitizedGerman = ArgumentSanitizer.sanitize(german);
        assertThat(sanitizedGerman).isEqualTo("Hallo {0}!");
    }


    @Test
    public void cleanupMultipleArgs() {
        String english = "Hello {0}! Welcome to the {1}.";
        String german = "Hallo {0!}! Willkommen im {1}.";

        String sanitizedEnglish = ArgumentSanitizer.sanitize(english);
        assertThat(sanitizedEnglish).isEqualTo(english);
        String sanitizedGerman = ArgumentSanitizer.sanitize(german);
        assertThat(sanitizedGerman).isEqualTo("Hallo {0}! Willkommen im {1}.");
    }
}
