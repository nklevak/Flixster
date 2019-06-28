package com.example.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    // instance variables which will track characteristics of each movie (from API)
    private String title;
    private String overview;
    private String posterPath; // NOT the full url, just the path
    private String backdropPath; // for landscape view

    // constructer so you can initialize movie object directly from JSON info
    public Movie (JSONObject object) throws JSONException {
        // key values found from API docs
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    // create get methods so you can access vars outside of class
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
