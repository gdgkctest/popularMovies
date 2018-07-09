package com.chuck.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuck.android.popularmovies.adapter.MovieAdapter;
import com.chuck.android.popularmovies.adapter.MovieReviewAdapter;
import com.chuck.android.popularmovies.adapter.MovieTrailerAdapter;
import com.chuck.android.popularmovies.models.MinMovie;
import com.chuck.android.popularmovies.models.Movie;
import com.chuck.android.popularmovies.models.MovieReview;
import com.chuck.android.popularmovies.models.MovieReviewList;
import com.chuck.android.popularmovies.models.MovieTrailer;
import com.chuck.android.popularmovies.models.MovieTrailerList;
import com.chuck.android.popularmovies.rest.MovieApi;
import com.chuck.android.popularmovies.rest.MovieInterface;
import com.chuck.android.popularmovies.viewmodels.DetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity {
    //Display Details about the clicked on movie from Main Activity
    private static final String TAG = MainActivity.class.getSimpleName();
    private DetailsViewModel mViewModel;
    protected Movie movieDetails;
     CheckBox checkbox;
     TextView movieTitle;
     TextView movieDate ;
     TextView movieRating ;
     TextView movieDescription ;
     ImageView moviePoster;
    private String title;
    private RecyclerView rv_Reviews;
    private RecyclerView rv_Trailers;

    private MovieReviewAdapter reviewAdapter;
    private MovieTrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //Get Passed Extra ID from intent
        int id = getIntent().getIntExtra("EXTRA_MOVIE_ID",0);
        //Define Views
        movieTitle = findViewById(R.id.movieTitle);
         movieDate = findViewById(R.id.movieDate);
         movieRating = findViewById(R.id.movieRating);
         movieDescription = findViewById(R.id.movieDescription);
         moviePoster = findViewById(R.id.movieDetailsPoster);
        checkbox = findViewById(R.id.ck_favorites);

        retrieveMovieDetails(id);
        initViewModel(id);
        initRecylerViews();

        checkbox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkbox.isChecked())
                {
                    Toast.makeText(getApplicationContext(),"Item Added",Toast.LENGTH_SHORT).show();
                    mViewModel.addMovie(movieDetails.getId(),movieDetails.getTitle(),movieDetails.getPosterPath());

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Item Deleted",Toast.LENGTH_SHORT).show();
                    mViewModel.deleteMovie();

                }
            }
        });

    }

    private void initRecylerViews() {
        rv_Reviews = findViewById(R.id.rv_reviewList);
        rv_Trailers = findViewById(R.id.rv_trailerList);
        rv_Reviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_Trailers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        reviewAdapter = new MovieReviewAdapter();
        trailerAdapter = new MovieTrailerAdapter();
        rv_Reviews.setAdapter(reviewAdapter);
        rv_Trailers.setAdapter(trailerAdapter);
    }

    private void initViewModel(int id) {
        mViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        mViewModel.mLiveMovie.observe(this, new Observer<MinMovie>() {
            @Override
            public void onChanged(@Nullable MinMovie movieTitleObject) {
                if (movieTitleObject != null)
                    checkbox.setChecked(true);
            }
        });
        mViewModel.loadData(id);

    }

    private void retrieveMovieDetails(int id) {
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
                movieDetails = response.body();
                //Get movie Poster
                String PosterPath = "http://image.tmdb.org/t/p/w500" + movieDetails.getPosterPath();
                Picasso.get()
                        .load(PosterPath)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_image_error)
                        .into(moviePoster);
                moviePoster.setContentDescription(movieDetails.getTitle());
                //Get Movie Title, Date, Rating and Synopsis and Display
                movieTitle.setText(movieDetails.getTitle());
                movieDate.setText(movieDetails.getReleaseDate());
                movieRating.setText(Double.toString(movieDetails.getVoteAverage()));
                movieDescription.setText(movieDetails.getOverview());
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
                trailerAdapter.setMovies(movieTrailerList);
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
                reviewAdapter.setMovies(movieReviewList);
            }
            @Override
            public void onFailure(Call<MovieReviewList> call, Throwable t) {

            }
        });




    }
}