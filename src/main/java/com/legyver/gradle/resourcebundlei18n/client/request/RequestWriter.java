package com.legyver.gradle.resourcebundlei18n.client.request;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Writer to write request body
 */
public class RequestWriter {

    /**
     * Write request bodo
     * @param httpURLConnection the connection to write to
     * @param messageJson the content to write
     * @throws IOException if there is an error writing to the connection
     */
    public void write(HttpURLConnection httpURLConnection, String messageJson) throws IOException {
        httpURLConnection.setDoOutput(true);

        try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
            byte[] input = messageJson.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input);
        }
    }
}
