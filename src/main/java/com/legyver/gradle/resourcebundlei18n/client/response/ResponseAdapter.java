package com.legyver.gradle.resourcebundlei18n.client.response;

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
     */
    T adapt(String responseAsString);
}
