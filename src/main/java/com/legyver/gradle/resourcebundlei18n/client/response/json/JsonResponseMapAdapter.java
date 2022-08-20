package com.legyver.gradle.resourcebundlei18n.client.response.json;

import com.legyver.core.exception.CoreException;
import com.legyver.utils.jackiso.JacksonObjectMapper;

import java.util.Map;

public class JsonResponseMapAdapter implements JsonResponseAdapter<Map> {

    @Override
    public Map<String, Object> adapt(String responseAsString) throws CoreException {
        Map<String, Object> result = JacksonObjectMapper.INSTANCE.readValue(responseAsString, Map.class);
        return result;
    }
}
