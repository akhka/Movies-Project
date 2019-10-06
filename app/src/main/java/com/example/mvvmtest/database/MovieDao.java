package com.example.mvvmtest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvmtest.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie_table")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movie_table")
    public List<Movie> loadMovie();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Query("DELETE FROM movie_table WHERE id=:movieId")
    void deleteById(int movieId);



}
