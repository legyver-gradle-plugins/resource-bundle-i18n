package com.legyver.gradle.resourcebundlei18n.client;

import com.legyver.gradle.resourcebundlei18n.client.api.DetectLanguageApiStrategy;
import com.legyver.gradle.resourcebundlei18n.client.api.TranslationApi;
import com.legyver.gradle.resourcebundlei18n.client.api.TranslationApiStrategy;
import com.legyver.gradle.resourcebundlei18n.client.request.RequestWriter;
import com.legyver.gradle.resourcebundlei18n.client.response.ResponseReader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Client {
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

    public String detectLanguage(String value) throws IOException, URISyntaxException {
        translationApi.setStrategy(new DetectLanguageApiStrategy());
        //curl -X POST "http://localhost:5000/detect" -H  "accept: application/json" -H  "Content-Type: application/x-www-form-urlencoded"
        // -d "q=Hello%20world!&api_key=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"

        String languageDetectionEndpoint = translationApi.getEndpoint();
        if (!languageDetectionEndpoint.startsWith("/")) {
            languageDetectionEndpoint = "/" + languageDetectionEndpoint;
        }
        URL endpoint = append(languageDetectionEndpoint);
        HttpURLConnection httpURLConnection = (HttpURLConnection) endpoint.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.addRequestProperty("accept", translationApi.getAcceptedType());
        httpURLConnection.addRequestProperty("Content-Type", translationApi.getContentType());

        String message = translationApi.makeRequestBody(value);
        requestWriter.write(httpURLConnection, message);

        String responseAsString = responseReader.read(httpURLConnection);
        return translationApi.getResult(responseAsString);
    }

    public String getTranslation(String textToTranslate, String sourceLanguage, String targetLanguage) throws IOException {
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
