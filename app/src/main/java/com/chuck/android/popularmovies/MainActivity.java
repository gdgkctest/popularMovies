package com.chuck.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chuck.android.popularmovies.adapter.MovieAdapter;
import com.chuck.android.popularmovies.models.MinMovie;
import com.chuck.android.popularmovies.models.Movie;
import com.chuck.android.popularmovies.models.MovieList;
import com.chuck.android.popularmovies.rest.MovieApi;
import com.chuck.android.popularmovies.rest.MovieInterface;
import com.chuck.android.popularmovies.viewmodels.MainViewModel;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //used in error logs
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    StaggeredGridLayoutManager movieGridLayoutManger;
    public static final String BUNDLE_RV_POSITION = "Bundle_RV_POS_KEY";
    public static final String SHARED_PREF_LISTTYPE = "List Type Preference";

    private String selectedList;
    private List<MinMovie> currentMoviesList = new ArrayList<>();
    private List<MinMovie> favoriteMovieList;
    private MainViewModel mViewModel;

    public static final String FAVORITESMOVIES = "Favorites";
    public static final String POPULARMOVIES = "Popular Movies";
    public static final String TOPRATEDMOVIES = "Top Rated Movies";

    private String movieListType;
    private Parcelable savedRecyclerLayoutState;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.movieDbList);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        preferences = getApplicationContext().getSharedPreferences("ListType",0);
        movieListType = preferences.getString(SHARED_PREF_LISTTYPE,"");

        initViewModel();
        initRecyclerView();
        Stetho.initializeWithDefaults(this);

        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RV_POSITION);
     //       if (!movieListType.equals(FAVORITESMOVIES))
                recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        //if saved state is null select popular movies
        else {
            movieListSelect(POPULARMOVIES);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RV_POSITION, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences.Editor editor = preferences.edit();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            selectedList = "Popular Movies";
            editor.putString(SHARED_PREF_LISTTYPE,POPULARMOVIES);
        } else if (id == R.id.action_topRated) {
            selectedList = "Top Rated Movies";
            editor.putString(SHARED_PREF_LISTTYPE,TOPRATEDMOVIES);
        } else if (id == R.id.action_favorites) {
            selectedList = "Favorites";
            editor.putString(SHARED_PREF_LISTTYPE,FAVORITESMOVIES);
        }
        editor.commit();
        movieListSelect(selectedList);
        return super.onOptionsItemSelected(item);
    }

    public void movieListSelect(final String movieList) {
        //Defines the Parameters for the API call to movieDB
        MovieInterface movieService = MovieApi.getClient().create(MovieInterface.class);
        //Store API Key
        String apiKey = getString(R.string.API_key);
        //Reviewer: Please Enter API Key in secrets.xml for moviedb
        if (apiKey.equals(""))
            Toast.makeText(this, R.string.api_error_message, Toast.LENGTH_SHORT).show();
            //Only Run if API key is available
        else {
            if (FAVORITESMOVIES.equals(movieList)) {
                mViewModel.getCurrentMovies().postValue(favoriteMovieList);
                recyclerView.scrollToPosition(0);

            } else {
                //Choose a retrofit call based on sort option
                Call<MovieList> call;
                if (POPULARMOVIES.equals(movieList))
                    call = movieService.getPopularMovies(apiKey);
                else if (TOPRATEDMOVIES.equals(movieList))
                    call = movieService.getTopRatedMovies(apiKey);
                else
                    call = movieService.getTopRatedMovies(apiKey);

                //Send the network call using Retrofit to background task
                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        //If Successful Network Call to API bind the Recycleview to MovieAdaptor
                        List<Movie> movies = response.body().getResults();
                        currentMoviesList.clear();
                        for (Movie movie : movies)
                            currentMoviesList.add(new MinMovie(movie.getId(), movie.getTitle(), movie.getPosterPath()));
                        mViewModel.getCurrentMovies().postValue(currentMoviesList);
                        recyclerView.scrollToPosition(0);


                    }
                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        //If not log the Error
                        Log.e(TAG, t.toString());
                    }
                });
            }
        }
    }

    private void initRecyclerView() {
        movieGridLayoutManger = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(movieGridLayoutManger);
        adapter = new MovieAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        final Observer<List<MinMovie>> moviesObserver =
                new Observer<List<MinMovie>>() {
                    @Override
                    public void onChanged(@Nullable List<MinMovie> movies) {
                        initRecyclerView();
                        adapter.setMovies(movies);
                    }
                };
        final Observer<List<MinMovie>> favoriteMoviesObserver =
                new Observer<List<MinMovie>>() {
                    @Override
                    public void onChanged(@Nullable List<MinMovie> movies) {
                        favoriteMovieList = movies;
                        if (movieListType.equals(FAVORITESMOVIES)) {
                              //  mViewModel.getCurrentMovies().postValue(movies);
                            adapter.setMovies(movies);
                        }

                    }
                };
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getCurrentMovies().observe(this, moviesObserver);
        mViewModel.getFavoriteMovies().observe(this, favoriteMoviesObserver);
    }
}