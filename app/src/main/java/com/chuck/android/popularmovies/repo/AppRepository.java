package com.chuck.android.popularmovies.repo;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.chuck.android.popularmovies.DB.AppDatabase;
import com.chuck.android.popularmovies.MainActivity;
import com.chuck.android.popularmovies.models.MinMovie;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static AppRepository instance;

    //Used for async tasks
    private Executor executor = Executors.newSingleThreadExecutor();
    //List retrieved from DB
    public LiveData<List<MinMovie>> favoriteMovies;

    private AppDatabase movDB;

    public static AppRepository getInstance(Context context) {
        //Only one Repository allowed
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;

    }

    private AppRepository(Context context) {
        //init Repo and assign db content to livedata
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

                try {
                    movDB.movieDao().insertMovie(movie);
                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    public void deleteFavoriteMovie(final MinMovie movie) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    movDB.movieDao().deleteMovie(movie);
            }catch (Exception e){
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

}



