package com.mooveit.greetings;

import static java.net.HttpURLConnection.HTTP_OK;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mooveit.greetings.model.EmployeeJson;
import com.mooveit.greetings.model.EmployeeJsonParser;
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

public class GreetingsLambdaHandler implements RequestHandler<GatewayRequest, GatewayResponse> {

    private static final String EMPLOYEE_SERVICE_URL = "http://dummy.restapiexample.com/api/v1/employee/1";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();
    private static final EmployeeJsonParser EMPLOYEE_JSON_PARSER = new EmployeeJsonParser(new JSONParser());

    @SuppressWarnings("unchecked")
    @Override
    public GatewayResponse handleRequest(GatewayRequest gatewayRequest, Context context) {
        String greetingString = String.format("Hello %s %s", gatewayRequest.getFirstName(), gatewayRequest.getLastName());

        greetingString += gatewayRequest.getAge() != null ? ", your age is: " + gatewayRequest.getAge() : getAgeMessage(context);

        JSONObject responseBodyJson = new JSONObject();
        responseBodyJson.put("greetings", greetingString);

        return new GatewayResponse(
                responseBodyJson.toJSONString(),
                Collections.singletonMap("Content-Type", "application/json"),
                HTTP_OK);
    }

    private String getAgeMessage(Context context) {
        // REST call to employee API
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(EMPLOYEE_SERVICE_URL))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .timeout(Duration.ofMillis(1200))
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HTTP_OK) {

                EmployeeJson employeeJson = EMPLOYEE_JSON_PARSER.parse(response.body());
                return  ", your age is: " + employeeJson.getAge();
            }
        } catch (IOException | InterruptedException e) {
            final LambdaLogger logger = context.getLogger();
            logger.log("Error: " + Arrays.toString(e.getStackTrace()));
        }
        return "";
    }
}
