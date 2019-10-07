package com.example.mvvmtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvmtest.adapters.ReviewRecyclerAdapter;
import com.example.mvvmtest.adapters.TrailerOnItemClickListener;
import com.example.mvvmtest.adapters.TrailerRecyclerAdapter;
import com.example.mvvmtest.model.Movie;
import com.example.mvvmtest.model.Review;
import com.example.mvvmtest.model.ReviewResponse;
import com.example.mvvmtest.model.Trailer;
import com.example.mvvmtest.model.TrailerResponse;
import com.example.mvvmtest.network.ApiClient;
import com.example.mvvmtest.network.ApiService;
import com.example.mvvmtest.viewmodel.MovieViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mvvmtest.utils.Constants.IMAGE_BASE_URL;
import static com.example.mvvmtest.utils.Constants.TMDB_API_KEY;

public class DetailsActivity extends AppCompatActivity implements TrailerOnItemClickListener {

    private int movieId;
    private boolean isFav = false;
    private MovieViewModel viewModel;
    private List<Review> reviews = new ArrayList<>();
    private List<Trailer> trailers = new ArrayList<>();

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
        recyclerView_trailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TrailerRecyclerAdapter trailerAdapter = new TrailerRecyclerAdapter();
        trailerAdapter.setOnItemClickListener(this);
        recyclerView_trailers.setAdapter(trailerAdapter);
        recyclerView_reviews = findViewById(R.id.recyclerView_reviews);
        recyclerView_reviews.setLayoutManager(new LinearLayoutManager(this));
        ReviewRecyclerAdapter reviewAdapter = new ReviewRecyclerAdapter();
        recyclerView_reviews.setAdapter(reviewAdapter);



        final Intent intent = getIntent();

        movieId = intent.getIntExtra("id", 0);

        Picasso.get().load(IMAGE_BASE_URL + intent.getStringExtra("posterUrl")).into(imageView_details);

        textView_detail_title.setText(intent.getStringExtra("title"));
        textView_detail_overview.setText(intent.getStringExtra("overview"));
        textView_detail_avgRate.setText(String.valueOf(intent.getFloatExtra("rating", 0)) + "  of  10");
        textView_detail_release.setText(intent.getStringExtra("releaseDate"));

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
                    Toast.makeText(DetailsActivity.this, "Removed from Favorite", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DetailsActivity.this, "Adeed to Favorite", Toast.LENGTH_SHORT).show();
                }

                progressBar_favorite_check.setVisibility(View.GONE);
                imageView_favorited.setVisibility(View.VISIBLE);
            }
        });

        getMovieReviews(movieId, (ReviewRecyclerAdapter) recyclerView_reviews.getAdapter());
        getMovieTrailers(movieId, (TrailerRecyclerAdapter) recyclerView_trailers.getAdapter());


    }


    public void getMovieReviews(int movieId, final ReviewRecyclerAdapter adapter){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ReviewResponse> call = apiService.getReviews(movieId, TMDB_API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(DetailsActivity.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                ReviewResponse result = response.body();
                reviews = result.getReviews();
                System.out.println(reviews.get(0).getAuthor());
                adapter.setReviews(reviews);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    public void getMovieTrailers(int movieId, final TrailerRecyclerAdapter adapter){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<TrailerResponse> call = apiService.getTrailers(movieId, TMDB_API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(DetailsActivity.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                TrailerResponse result = response.body();
                trailers = result.getTrailers();
                adapter.setTrailers(trailers);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClickListener(int position) {
        Trailer trailer = trailers.get(position);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
        startActivity(intent);
    }
}
