package com.mooveit.greetings.model;

import org.json.simple.JSONObject;

public class AlbumJson {

    private final JSONObject album;

    public AlbumJson(JSONObject album) {
        this.album = album;
    }

    public String getId() {
        return getAlbumProperty("id");
    }

    public String getUserId() {
        return getAlbumProperty("userId");
    }

    public String getTitle() {
        return getAlbumProperty("title");
    }

    @SuppressWarnings("unchecked")
    private <T> T getAlbumProperty(String propertyName) {
        return (T) album.get(propertyName);
    }
}
