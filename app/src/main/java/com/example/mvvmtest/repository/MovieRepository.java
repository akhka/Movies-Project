package com.example.mvvmtest.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mvvmtest.database.AppDatabase;
import com.example.mvvmtest.database.MovieDao;
import com.example.mvvmtest.model.Movie;

import java.util.List;

public class MovieRepository {

    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;
    public List<Movie> movies;

    public MovieRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        movieDao = db.movieDao();
        allMovies = movieDao.getAllMovies();
    }


    public void insert(Movie movie){
        new InsertTask(movieDao).execute(movie);
    }


    public void deleteById(int movieId){
        new DeleteTask(movieDao).execute(movieId);
    }


    public List<Movie> getById(){
        GetTask gt = new GetTask(movieDao);
        gt.execute();
        movies = gt.getMovie();
        return movies;
    }


    public LiveData<List<Movie>> getAllMovies(){
        return allMovies;
    }


    private static class InsertTask extends AsyncTask<Movie, Void, Void>{

        MovieDao movieDao;

        public InsertTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insert(movies[0]);
            return null;
        }
    }



    private static class DeleteTask extends AsyncTask<Integer, Void, Void>{

        MovieDao movieDao;

        public DeleteTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }


        @Override
        protected Void doInBackground(Integer... integers) {
            movieDao.deleteById(integers[0]);
            return null;
        }
    }

    private static class GetTask extends AsyncTask<Void, Void, Void>{

        MovieDao movieDao;
        List<Movie> movie;

        private GetTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            movie = movieDao.loadMovie();
            return null;
        }

        public List<Movie> getMovie(){
            return movie;
        }
    }

}
