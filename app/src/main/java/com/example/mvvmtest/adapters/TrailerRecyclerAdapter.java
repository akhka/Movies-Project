package com.example.mvvmtest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmtest.R;
import com.example.mvvmtest.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvvmtest.utils.Constants.IMAGE_BASE_URL;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.ViewHolder> {


    private List<Trailer> trailers = new ArrayList<>();
    private TrailerOnItemClickListener listener;

    public void setOnItemClickListener(TrailerOnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trailer_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        System.out.println(trailer.getKey());
        Picasso.get().load("http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg").into(holder.imageView_trailer);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public void setTrailers(List<Trailer> trailers){
        this.trailers = trailers;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_trailer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_trailer = itemView.findViewById(R.id.imageView_trailer);
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
