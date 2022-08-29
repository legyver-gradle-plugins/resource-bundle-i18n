package com.legyver.gradle.resourcebundlei18n.client;

import com.legyver.core.exception.CoreException;
import com.legyver.gradle.resourcebundlei18n.client.api.DetectLanguageApiStrategy;
import com.legyver.gradle.resourcebundlei18n.client.api.TranslationApi;
import com.legyver.gradle.resourcebundlei18n.client.api.TranslationApiStrategy;
import com.legyver.gradle.resourcebundlei18n.client.request.RequestWriter;
import com.legyver.gradle.resourcebundlei18n.client.response.ResponseReader;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {
    private static final Logger logger = Logging.getLogger(Client.class);

    private final URL url;
    private final TranslationApi translationApi;
    private final RequestWriter requestWriter;
    private final ResponseReader responseReader;

    public Client(URL url, TranslationApi translationApi) {
        this.url = url;
        this.translationApi = translationApi;
        this.requestWriter = new RequestWriter();
        this.responseReader = new ResponseReader();
    }

    public String detectLanguage(String value) throws CoreException {
        translationApi.setStrategy(new DetectLanguageApiStrategy());
        //curl -X POST "http://localhost:5000/detect" -H  "accept: application/json" -H  "Content-Type: application/x-www-form-urlencoded"
        // -d "q=Hello%20world!&api_key=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"

        String languageDetectionEndpoint = translationApi.getEndpoint();
        if (!languageDetectionEndpoint.startsWith("/")) {
            languageDetectionEndpoint = "/" + languageDetectionEndpoint;
        }

        HttpURLConnection httpURLConnection = null;
        try {
            URL endpoint = append(languageDetectionEndpoint);
            httpURLConnection = (HttpURLConnection) endpoint.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.addRequestProperty("accept", translationApi.getAcceptedType());
            httpURLConnection.addRequestProperty("Content-Type", translationApi.getContentType());

            String message = translationApi.makeRequestBody(value);
            logger.debug("Sending message: {}", message);
            requestWriter.write(httpURLConnection, message);

            String responseAsString = responseReader.read(httpURLConnection);
            logger.debug("Received response: {}", responseAsString);
            return translationApi.getResult(responseAsString);
        } catch (IOException e) {
            if (httpURLConnection != null) {
                try {
                    String error = responseReader.readError(httpURLConnection);
                    logger.error("Error receiving communicating with server: {}", error);
                    throw new CoreException("Client Error: " + error);
                } catch (IOException ex) {
                    logger.error("Error attempting to read error", ex);
                    throw new CoreException("Unable to read error message", ex);
                }
            } else {
                logger.error("Error received: " + e.getMessage(), e);
            }
            throw new CoreException(e);
        }

    }

    public String getTranslation(String textToTranslate, String sourceLanguage, String targetLanguage) throws IOException, CoreException {
        translationApi.setStrategy(new TranslationApiStrategy());
        //curl -X POST "http://localhost:5000/translate" -H  "accept: application/json" -H  "Content-Type: application/x-www-form-urlencoded"
        // -d "q=hello&source=en&target=es&format=text&api_key=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
        String translationEndpoint = translationApi.getEndpoint();
        URL endpoint = append(translationEndpoint);
        HttpURLConnection httpURLConnection = (HttpURLConnection) endpoint.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.addRequestProperty("accept", translationApi.getAcceptedType());
        httpURLConnection.addRequestProperty("Content-Type", translationApi.getContentType());

        String message = translationApi.makeRequestBody(textToTranslate, sourceLanguage, targetLanguage);
        requestWriter.write(httpURLConnection, message);
        String responseAsString = responseReader.read(httpURLConnection);
        return translationApi.getResult(responseAsString);
    }

    private URL append(String endpoint) throws MalformedURLException {
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath() + endpoint, null);
    }

}
