package com.example.mvvmtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.mvvmtest.adapters.MovieListAdapter;
import com.example.mvvmtest.database.AppDatabase;
import com.example.mvvmtest.database.MovieDao;
import com.example.mvvmtest.model.Movie;
import com.example.mvvmtest.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recycler_movies = findViewById(R.id.recycler_movies);
        recycler_movies.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_movies.setHasFixedSize(true);

        final MovieListAdapter adapter = new MovieListAdapter();
        recycler_movies.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });

    }
}
