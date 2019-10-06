package com.example.mvvmtest.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvmtest.model.Movie;
import com.example.mvvmtest.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> allMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        allMovies = repository.getAllMovies();
    }


    public void insert(Movie movie){
        repository.insert(movie);
    }


    public void deleteById(int id){
        repository.deleteById(id);
    }


    public LiveData<List<Movie>> getAllMovies(){
        return allMovies;
    }
}
