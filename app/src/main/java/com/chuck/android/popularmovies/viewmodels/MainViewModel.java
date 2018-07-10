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
    //Data Object for currently Displayed List of movies
    private MutableLiveData<List<MinMovie>> movieList;
    //Data Object for List of Favorites
    private LiveData<List<MinMovie>> favoriteMovieList;
    private AppRepository mRepository;


    public MainViewModel(@NonNull Application application) {
        //On init of viewmodel retrieve favorite movies
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        favoriteMovieList = mRepository.getAllMovies();
    }

    //Returns Currently Displayed movies if initialized
    public MutableLiveData<List<MinMovie>> getCurrentMovies() {
        //Creates new list of movies if empty
        if (movieList == null) {
            movieList = new MutableLiveData<>();
        }
        return movieList;
    }

    //Return Favorites
    public LiveData<List<MinMovie>> getFavoriteMovies() {
        return favoriteMovieList;
    }


}
