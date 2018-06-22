package com.chuck.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuck.android.popularmovies.models.Movie;
import com.chuck.android.popularmovies.models.MovieReview;
import com.chuck.android.popularmovies.models.MovieReviewList;
import com.chuck.android.popularmovies.models.MovieTrailer;
import com.chuck.android.popularmovies.models.MovieTrailerList;
import com.chuck.android.popularmovies.rest.MovieApi;
import com.chuck.android.popularmovies.rest.MovieInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity {
    //Display Details about the clicked on movie from Main Activity
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //Get Passed Extra ID from intent
        int id = getIntent().getIntExtra("EXTRA_MOVIE_ID",0);
        //Define Views
        final TextView movieTitle = findViewById(R.id.movieTitle);
        final TextView movieDate = findViewById(R.id.movieDate);
        final TextView movieRating = findViewById(R.id.movieRating);
        final TextView movieDescription = findViewById(R.id.movieDescription);
        final ImageView moviePoster = findViewById(R.id.movieDetailsPoster);

        //Create Retrofit API object for movieDB api
        MovieInterface movieService = MovieApi.getClient().create(MovieInterface.class);

        String apiKey = getString(R.string.API_key);
        //Populate Retrofit API with parameters
        Call<Movie> call = movieService.getMovieDetails(id,apiKey);

        call.enqueue(new Callback<Movie>() {
            @Override
            //ON background thread send API call and retrieve JSON object
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                //Retrieve all information into movie object
                Movie movie = response.body();
                //Get movie Poster
                String PosterPath = "http://image.tmdb.org/t/p/w500" + movie.getPosterPath();
                Picasso.get()
                        .load(PosterPath)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_image_error)
                        .into(moviePoster);
                moviePoster.setContentDescription(movie.getTitle());
                //Get Movie Title, Date, Rating and Synopsis and Display
                movieTitle.setText(movie.getTitle());
                movieDate.setText(movie.getReleaseDate());
                movieRating.setText(Double.toString(movie.getVoteAverage()));
                movieDescription.setText(movie.getOverview());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
        Call<MovieTrailerList> callTrailerList = movieService.getMovieTrailers(id,apiKey);

        callTrailerList.enqueue(new Callback<MovieTrailerList>() {
            @Override
            public void onResponse(Call<MovieTrailerList> call, Response<MovieTrailerList> response) {
                List<MovieTrailer> movieTrailerList = response.body().getResults();
                TextView movieTrailer = findViewById(R.id.movieTrailer);
                for (MovieTrailer trailer: movieTrailerList )
                {
                    String youTubeShortCode = trailer.getKey();
                    movieTrailer.append(trailer.getName() + "\n");
                    movieTrailer.append("https://youtu.be/" + youTubeShortCode + "\n");
                    Linkify.addLinks(movieTrailer, Linkify.WEB_URLS);
                }
            }
            @Override
            public void onFailure(Call<MovieTrailerList> call, Throwable t) {

            }
        });
            Call<MovieReviewList> callReviewList = movieService.getMovieReviews(id,apiKey);

        callReviewList.enqueue(new Callback<MovieReviewList>() {
                @Override
                public void onResponse(Call<MovieReviewList> call, Response<MovieReviewList> response) {
                    List<MovieReview> movieReviewList = response.body().getResults();
                    TextView movieReview = findViewById(R.id.movieReview);
                    for (MovieReview review: movieReviewList )
                    {
                        movieReview.append(review.getContent() + "\n");
                    }
                }
            @Override
            public void onFailure(Call<MovieReviewList> call, Throwable t) {

            }
        });



    }
}