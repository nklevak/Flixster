package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // constants
    // base url for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    // instance fields
    AsyncHttpClient client;
    // list of movies that are currently playing
    ArrayList<Movie> movies;
    // the recycler view
    RecyclerView rvMovies;
    // creates an adapter connected to the recycler view
    MovieAdapter adapter;
    // image config object (to track configuration)
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // initialize client
        client = new AsyncHttpClient();

        // initialize list of movies that are currently playing before doing network creation
        movies = new ArrayList<>();

        // initialize the adapter
        // has to be initialized after movies is created, since movies is passed to it
        // movies adapter can't be reinitialized after this point, just modified
        adapter = new MovieAdapter(movies);

        // connect everything (layout manager, adapter) and resolve the recycler view
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // get the configuration upon app creation
        getConfiguration();

    }

    // get list of currently playing movies from the API
    // looks similar to getConfiguration() because we are accessing the API again
    private void getNowPlaying() {
        // create the url
        String url = API_BASE_URL + "/movie/now_playing";
        // set the request parameters
        RequestParams params = new RequestParams();
        // call getString because R.string.api_key is the numeric ID of the key, not the key itself
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        // code you're passing will get response at ambiguous later time in the future
        // make request, mark that its pending, get result, implement result
        // this part executes a GET request using the internet, and gets JSON object in response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // iterate through results and creates Movie object for each movie
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify adapter that the dataset changed every time we add a movie to the list
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now_playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }

    // get configuration from the API
    private void getConfiguration() {
        // create the url
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        // call getString because R.string.api_key is the numeric ID of the key, not the key itself
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // initialize config
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and PosterSize %s", config.getImageBaseUrl(),config.getPosterSize()));
                    // pass config to adapter
                    adapter.setConfig(config);
                    // call it here to make sure get configuration loads/returns before getNowPlaying does
                    // get now playing list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    // handle errors using logging, alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        Log.e(TAG, message, error);

        // alert user
        if (alertUser) {
            // show long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
