package com.example.mvvmtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mvvmtest.model.Movie;
import com.example.mvvmtest.viewmodel.MovieViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvvmtest.utils.Constants.IMAGE_BASE_URL;

public class DetailsActivity extends AppCompatActivity {

    private int movieId;
    private boolean isFav = false;
    private MovieViewModel viewModel;
    private List<Movie> checkMovie;

    ImageView imageView_details;
    ProgressBar progressBar_favorite_check;
    ImageView imageView_favorited;
    TextView textView_detail_title;
    TextView textView_detail_overview;
    TextView textView_detail_avgRate;
    TextView textView_detail_release;

    RecyclerView recyclerView_trailers;
    RecyclerView recyclerView_reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        imageView_details = findViewById(R.id.imageView_details);
        progressBar_favorite_check = findViewById(R.id.progressBar_favorite_check);
        progressBar_favorite_check.setVisibility(View.VISIBLE);
        imageView_favorited = findViewById(R.id.imageView_favorited);
        imageView_favorited.setVisibility(View.GONE);
        textView_detail_title = findViewById(R.id.textView_detail_title);
        textView_detail_overview = findViewById(R.id.textView_detail_overview);
        textView_detail_avgRate = findViewById(R.id.textView_detail_avgRate);
        textView_detail_release = findViewById(R.id.textView_detail_release);

        recyclerView_trailers = findViewById(R.id.recyclerView_trailers);
        recyclerView_reviews = findViewById(R.id.recyclerView_reviews);



        final Intent intent = getIntent();

        movieId = intent.getIntExtra("id", 0);

        Picasso.get().load(IMAGE_BASE_URL + intent.getStringExtra("posterUrl")).into(imageView_details);

        if (intent.getBooleanExtra("isFav", false) == true){
            imageView_favorited.setImageResource(R.drawable.ic_added);
            isFav = true;
        }
        else {
            imageView_favorited.setImageResource(R.drawable.ic_not_added);
            isFav = false;
        }

        progressBar_favorite_check.setVisibility(View.GONE);
        imageView_favorited.setVisibility(View.VISIBLE);

        imageView_favorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView_favorited.setVisibility(View.GONE);
                progressBar_favorite_check.setVisibility(View.VISIBLE);
                if (isFav){
                    viewModel.deleteById(movieId);
                    imageView_favorited.setImageResource(R.drawable.ic_not_added);
                    isFav = false;
                }
                else {
                    Movie movie = new Movie();
                    movie.setId(movieId);
                    movie.setTitle(intent.getStringExtra("title"));
                    movie.setOverview(intent.getStringExtra("overview"));
                    movie.setVoteAverage(intent.getFloatExtra("rating", 0));
                    movie.setReleaseDate(intent.getStringExtra("releaseDate"));
                    movie.setPosterUrl(intent.getStringExtra("posterUrl"));
                    movie.setFavorite(!isFav);
                    viewModel.insert(movie);
                    imageView_favorited.setImageResource(R.drawable.ic_added);
                    isFav = true;
                }

                progressBar_favorite_check.setVisibility(View.GONE);
                imageView_favorited.setVisibility(View.VISIBLE);
            }
        });

    }
}
