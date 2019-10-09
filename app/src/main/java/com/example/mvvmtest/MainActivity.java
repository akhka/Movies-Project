package com.example.mvvmtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mvvmtest.adapters.MovieListAdapter;
import com.example.mvvmtest.adapters.RecyclerOnItemClickListener;
import com.example.mvvmtest.model.Movie;
import com.example.mvvmtest.model.MovieResponse;
import com.example.mvvmtest.network.ApiClient;
import com.example.mvvmtest.network.ApiService;
import com.example.mvvmtest.viewmodel.MovieViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mvvmtest.utils.Constants.LANGUAGE;
import static com.example.mvvmtest.utils.Constants.TMDB_API_KEY;

public class MainActivity extends AppCompatActivity implements RecyclerOnItemClickListener {

    // TODO: Go to Constants.java and put your own TMDB API key


    private MovieViewModel viewModel;
    private List<Movie> mMovies;

    private BottomNavigationView bottomNav_home;
    private RecyclerView recycler_movies;
    private RecyclerView.LayoutManager lManager;
    private  MovieListAdapter adapter;

    private int SELECTED = R.id.popularMenu;

    private Parcelable savedRecyclerLayout;

    private ArrayList<Movie> moviesInstance = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            SELECTED = savedInstanceState.getInt("menu_id");
            moviesInstance = savedInstanceState.getParcelableArrayList("list_state");
            savedRecyclerLayout = savedInstanceState.getParcelable("recycler_layout");
        }

        bottomNav_home = findViewById(R.id.bottomNav_home);
        bottomNav_home.setOnNavigationItemSelectedListener(onNavListener);

        recycler_movies = findViewById(R.id.recycler_movies);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            lManager = new GridLayoutManager(this, 2);
        } else {
            lManager = new GridLayoutManager(this, 4);
        }
        recycler_movies.setLayoutManager(lManager);
        recycler_movies.setHasFixedSize(true);

        adapter = new MovieListAdapter();
        adapter.setOnItemClickListener(this);
        recycler_movies.setAdapter(adapter);

        if (savedRecyclerLayout != null){
            recycler_movies.getLayoutManager().onRestoreInstanceState(savedRecyclerLayout);
        }

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        populateUI(recycler_movies);

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("menu_id", SELECTED);

        outState.putParcelableArrayList("list_state", moviesInstance);
        outState.putParcelable("recycler_layout", recycler_movies.getLayoutManager().onSaveInstanceState());

    }


    private BottomNavigationView.OnNavigationItemSelectedListener onNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.popularMenu: {
                    SELECTED = R.id.popularMenu;
                    populateUI(recycler_movies);
                    System.out.println(SELECTED);
                    return true;
                }
                case R.id.topRatedMenu: {
                    SELECTED = R.id.topRatedMenu;
                    populateUI(recycler_movies);
                    System.out.println(SELECTED);
                    return true;
                }
                case R.id.favoriteMenue: {
                    SELECTED = R.id.favoriteMenue;
                    populateUI(recycler_movies);
                    System.out.println(SELECTED);
                    return true;
                }
            }
            return false;
        }
    };



    public void populateUI(RecyclerView recyclerView) {
        String sort = "popular";
        if (SELECTED == R.id.popularMenu) {
            sort = "popular";
        } else if (SELECTED == R.id.topRatedMenu)
            sort = "top_rated";
        else
            sort = "favorite";


        if (sort.equals("favorite")) {

            getFav((MovieListAdapter) recyclerView.getAdapter());

        } else {
            getData(sort, (MovieListAdapter) recyclerView.getAdapter());
        }

    }


    public void getData(String sort_type, final MovieListAdapter adapter) {
        //Toast.makeText(this, "Loading " + sort_type, Toast.LENGTH_SHORT).show();
        if (moviesInstance.size() != 0){
            adapter.setMovies(moviesInstance);
        }
        else {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<MovieResponse> call = apiService.getMovies(sort_type, TMDB_API_KEY, LANGUAGE);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    MovieResponse result = response.body();
                    List<Movie> list = result.getMovies();
                    moviesInstance = (ArrayList<Movie>) list;
                    adapter.setMovies(list);
                    mMovies = list;
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public void getFav(final MovieListAdapter adapter){
        viewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
                mMovies = movies;
            }
        });
    }

    @Override
    public void onItemClickListener(int position) {
        Intent detailIntent = new Intent(this, DetailsActivity.class);
        Movie clickedMovie = moviesInstance.get(position);
        detailIntent.putExtra("id", clickedMovie.getId());
        detailIntent.putExtra("title", clickedMovie.getTitle());
        detailIntent.putExtra("overview", clickedMovie.getOverview());
        detailIntent.putExtra("rating", clickedMovie.getVoteAverage());
        detailIntent.putExtra("releaseDate", clickedMovie.getReleaseDate());
        detailIntent.putExtra("posterUrl", clickedMovie.getPosterUrl());
        detailIntent.putExtra("isFav", clickedMovie.isFavorite());
        startActivity(detailIntent);
    }

}
