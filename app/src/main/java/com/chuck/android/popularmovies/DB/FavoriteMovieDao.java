package com.chuck.android.popularmovies.DB;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.chuck.android.popularmovies.models.MinMovie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MinMovie movieTitle);

    @Delete
    void deleteMovie(MinMovie movieTitle);

    @Query("SELECT * FROM movies WHERE id = :id")
    MinMovie getMovieByID(int id);

    @Query("SELECT * FROM movies")
    LiveData<List<MinMovie>> getAll();

}
