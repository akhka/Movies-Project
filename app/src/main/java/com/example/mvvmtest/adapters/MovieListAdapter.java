package com.example.mvvmtest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmtest.R;
import com.example.mvvmtest.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvvmtest.utils.Constants.IMAGE_BASE_URL;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>{

    private List<Movie> movies = new ArrayList<>();
    private RecyclerOnItemClickListener listener;


    public void setOnItemClickListener(RecyclerOnItemClickListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie currentMovie = movies.get(position);
        Picasso.get().load(IMAGE_BASE_URL + currentMovie.getPosterUrl()).into(holder.imageView_movie_list);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_movie_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_movie_list = itemView.findViewById(R.id.imageView_movie_list);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClickListener(position);
                        }
                    }
                }
            });
        }
    }
}
