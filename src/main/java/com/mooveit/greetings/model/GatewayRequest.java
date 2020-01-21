package com.mooveit.greetings.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GatewayRequest {

    private Map<String, String> queryStringParameters = new HashMap<>();

    public Map<String, String> getQueryStringParameters() {
        return queryStringParameters;
    }

    public void setQueryStringParameters(Map<String, String> queryStringParameters) {
        this.queryStringParameters = Objects.requireNonNullElse(queryStringParameters, new HashMap<>());
    }

    public String getFirstName() {
        return getParamOrDefault("firstName", "Ciccio");
    }

    public String getLastName() {
        return getParamOrDefault("lastName", "Palla");
    }

    public String getSentence() {
        return queryStringParameters.get("sentence");
    }

    private String getParamOrDefault(String paramName, String paramDefaultValue) {
        String paramValue = queryStringParameters.get(paramName);
        return Objects.requireNonNullElse(paramValue, paramDefaultValue);
    }
}
