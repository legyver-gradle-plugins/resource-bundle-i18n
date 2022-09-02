package com.legyver.gradle.resourcebundlei18n.client.response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Read a response from a HttpURLConnection
 */
public class ResponseReader {
    /**
     * Read a response from a HttpURLConnection
     *
     * @param connection the connection to read the response of
     * @return the response as a string
     * @throws IOException if there is an error getting the response
     */
    public String read(HttpURLConnection connection) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    public String readError(HttpURLConnection connection) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}
