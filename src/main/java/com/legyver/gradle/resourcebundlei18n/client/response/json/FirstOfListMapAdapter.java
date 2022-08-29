package com.legyver.gradle.resourcebundlei18n.client.response.json;

import com.legyver.core.exception.CoreException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FirstOfListMapAdapter implements JsonResponseAdapter<Map<String, Object>>{
    private final ListOfMapAdapter listOfMapAdapter;

    public FirstOfListMapAdapter(ListOfMapAdapter listOfMapAdapter) {
        this.listOfMapAdapter = listOfMapAdapter;
    }

    @Override
    public Map<String, Object> adapt(String responseAsString) throws CoreException {
        List<Map<String, Object>> result = listOfMapAdapter.adapt(responseAsString);
        return result.isEmpty() ? Collections.EMPTY_MAP : result.get(0);
    }
}
