package com.chuck.android.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.chuck.android.popularmovies.models.MinMovie;
import com.chuck.android.popularmovies.repo.AppRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailsViewModel extends AndroidViewModel {
    public MutableLiveData<MinMovie> mLiveMovie =  new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();


    public DetailsViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }
    public void loadData(final int movieId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MinMovie movie = mRepository.getFavoriteMovieById(movieId);
                mLiveMovie.postValue(movie);
            }
        });

    }
    public void deleteMovie() {
        mRepository.deleteFavoriteMovie(mLiveMovie.getValue());
    }
    public void addMovie(int id, String title,String posterPath){
        MinMovie newMovie = new MinMovie(id,title,posterPath);
        mRepository.insertFavoriteMovie(newMovie);
    }

}
