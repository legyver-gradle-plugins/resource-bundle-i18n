package com.legyver.gradle.resourcebundlei18n.client.response.json;

import com.legyver.core.exception.CoreException;
import com.legyver.utils.jackiso.JacksonObjectMapper;

import java.util.List;
import java.util.Map;

public class ListOfMapAdapter implements JsonResponseAdapter<List<Map<String, Object>>> {

    @Override
    public List<Map<String, Object>> adapt(String responseAsString) throws CoreException {
        List<Map<String, Object>> result = JacksonObjectMapper.INSTANCE.readValue(responseAsString, List.class);
        return result;
    }
}
