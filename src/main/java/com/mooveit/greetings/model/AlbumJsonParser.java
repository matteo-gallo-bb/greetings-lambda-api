package com.mooveit.greetings.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AlbumJsonParser {

    private final JSONParser jsonParser;

    public AlbumJsonParser(JSONParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public AlbumJson parse(String albumJsonString) {
        try {
            return new AlbumJson((JSONObject) jsonParser.parse(albumJsonString));
        } catch (ParseException e) {
            // TODO: use better Exception
            throw new RuntimeException("Error occurred during parsing!", e);
        }
    }
}
