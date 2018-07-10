package com.chuck.android.popularmovies.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.chuck.android.popularmovies.models.MinMovie;

@Database(entities = {MinMovie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    //creates the App's database
    public static final String DATABASE_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract FavoriteMovieDao movieDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
