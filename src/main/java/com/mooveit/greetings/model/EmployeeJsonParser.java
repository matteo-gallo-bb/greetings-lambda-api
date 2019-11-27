package com.mooveit.greetings.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EmployeeJsonParser {

    private final JSONParser jsonParser;

    public EmployeeJsonParser(JSONParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public EmployeeJson parse(String employeeJsonString) {
        try {
            return new EmployeeJson((JSONObject) jsonParser.parse(employeeJsonString));
        } catch (ParseException e) {
            // TODO: use better Exception
            throw new RuntimeException("Error occurred during parsing!", e);
        }
    }
}
