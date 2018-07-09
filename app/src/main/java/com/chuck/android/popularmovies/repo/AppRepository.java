package com.chuck.android.popularmovies.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.chuck.android.popularmovies.DB.AppDatabase;
import com.chuck.android.popularmovies.MainActivity;
import com.chuck.android.popularmovies.R;
import com.chuck.android.popularmovies.models.MinMovie;
import com.chuck.android.popularmovies.models.Movie;
import com.chuck.android.popularmovies.models.MovieList;
import com.chuck.android.popularmovies.rest.MovieApi;
import com.chuck.android.popularmovies.rest.MovieInterface;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static AppRepository instance;

    private Executor executor = Executors.newSingleThreadExecutor();
    public LiveData<List<MinMovie>> favoriteMovies;

    private AppDatabase movDB;

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;

    }
    private AppRepository(Context context) {
        movDB = AppDatabase.getInstance(context);
        favoriteMovies = getAllMovies();
    }
    public LiveData<List<MinMovie>> getAllMovies() {
        return movDB.movieDao().getAll();
    }
    public MinMovie getFavoriteMovieById(int movieID) {
        return movDB.movieDao().getMovieByID(movieID);
    }
    public void insertFavoriteMovie(final MinMovie movie) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                movDB.movieDao().insertMovie(movie);
            }
        });
    }
    public void deleteFavoriteMovie(final MinMovie movie) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                movDB.movieDao().deleteMovie(movie);
            }
        });
    }

}



