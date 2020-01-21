package com.mooveit.greetings;

import static java.net.HttpURLConnection.HTTP_OK;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mooveit.greetings.model.AlbumJson;
import com.mooveit.greetings.model.AlbumJsonParser;
import com.mooveit.greetings.model.GatewayRequest;
import com.mooveit.greetings.model.GatewayResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class GreetingsLambdaHandler implements RequestHandler<GatewayRequest, GatewayResponse> {

    private static final String ALBUM_SERVICE_URL = "http://jsonplaceholder.typicode.com/albums/";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();
    private static final AlbumJsonParser ALBUM_JSON_PARSER = new AlbumJsonParser(new JSONParser());
    private static final Random RANDOM = new Random();

    @SuppressWarnings("unchecked")
    @Override
    public GatewayResponse handleRequest(GatewayRequest gatewayRequest, Context context) {
        String greetingString = String.format("Hello %s %s", gatewayRequest.getFirstName(), gatewayRequest.getLastName());

        greetingString += gatewayRequest.getSentence() != null ? ", your sentence of the day: " + gatewayRequest.getSentence() : getSentence(context);

        JSONObject responseBodyJson = new JSONObject();
        responseBodyJson.put("greetings", greetingString);

        return new GatewayResponse(
                responseBodyJson.toJSONString(),
                Collections.singletonMap("Content-Type", "application/json"),
                HTTP_OK);
    }

    private String getSentence(Context context) {
        // REST call to employee API
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(ALBUM_SERVICE_URL + RANDOM.nextInt(100)))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .timeout(Duration.ofMillis(1200))
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HTTP_OK) {

                AlbumJson albumJson = ALBUM_JSON_PARSER.parse(response.body());
                return  ", your sentence of the day: " + albumJson.getTitle();
            }
        } catch (IOException | InterruptedException e) {
            final LambdaLogger logger = context.getLogger();
            logger.log("Error: " + Arrays.toString(e.getStackTrace()));
        }
        return "";
    }
}
