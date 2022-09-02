package com.legyver.gradle.resourcebundlei18n.client.response;

import com.legyver.core.exception.CoreException;

/**
 * Adapter to adapter the client response
 */
public interface ResponseAdapter<T> {
    /**
     * Get the accepted type to be included in the request "accept" header
     * @return the accepted type
     */
    String getAcceptedType();

    /**
     * Adapt the response from a String to the requisite type
     * @param responseAsString the response to adapt
     * @return the adapted response
     * @throws CoreException if there is an error
     */
    T adapt(String responseAsString) throws CoreException;
}
