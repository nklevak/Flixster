package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {
    // instance vars
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // get information from intent by unwrapping the movie
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("%s was clicked on and a new activity is being called'", movie.getTitle()));

        // assign instance vars
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);

        // set title and description
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // set rating for rating bar
        float avgRating = movie.getVoteAverage().floatValue();
        if (avgRating > 0) {
            rbVoteAverage.setRating(avgRating / 2.0f);
        }
        else {
            rbVoteAverage.setRating(0);
        }
    }
}