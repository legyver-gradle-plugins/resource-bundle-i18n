package com.legyver.gradle.resourcebundlei18n.client.request;

import java.text.MessageFormat;

public abstract class MessageFormatRequestAdapter implements RequestAdapter<Object[]> {
    private final String format;

    public MessageFormatRequestAdapter(String format) {
        this.format = format;
    }

    @Override
    public String getContentAsString(Object[] rawContent) {
        return MessageFormat.format(format, rawContent);
    }
}
