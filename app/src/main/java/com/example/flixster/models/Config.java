package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    // the base url for loading images
    String imageBaseUrl;
    // the poster size for when accessing images
    String posterSize;
    // the backdrop size
    String backdropSize;

    // constructor that accepts a JSONobject
    public Config(JSONObject object) throws JSONException {
        // getting it from an index allows for more flexibility-> better than hardcoding into code
        // this part is for parsing images portion of array
        JSONObject images = object.getJSONObject("images");
        // get the image base url
        imageBaseUrl = images.getString("secure_base_url");
        // get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use the option at index 3 (if it does not exist, use w342 as a fallback)
        posterSize = posterSizeOptions.optString(3, "w342");
        // parse backdrop sizes, use index 1 or w780 as fallback (if there is no index 1)
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1,"w780");
    }

    // helper method to help generate url
    public String getImageUrl(String size, String path) {
        // dont reference poster sie directly from class in case we want to use additional sizes later on
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    // generate getters for the vars
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
