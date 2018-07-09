package com.chuck.android.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.chuck.android.popularmovies.models.MinMovie;
import com.chuck.android.popularmovies.repo.AppRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<MinMovie>> movieList;
    private LiveData<List<MinMovie>> favoriteMovieList;
    private AppRepository mRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        favoriteMovieList = mRepository.getAllMovies();
    }

    public MutableLiveData<List<MinMovie>> getCurrentMovies() {
        if (movieList == null) {
            movieList = new MutableLiveData<>();
        }
        return movieList;
    }
    public LiveData<List<MinMovie>> getFavoriteMovies() {
        return favoriteMovieList;
    }


}
