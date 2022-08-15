package com.legyver.gradle.resourcebundlei18n.client.request.json;

import com.legyver.gradle.resourcebundlei18n.client.request.MessageFormatRequestAdapter;

public class MessageFormatJsonRequestAdapter extends MessageFormatRequestAdapter {
    public MessageFormatJsonRequestAdapter(String format) {
        super(format);
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String getContentAsString(Object[] rawContent) {
        return "{" + super.getContentAsString(rawContent) + "}";
    }
}
