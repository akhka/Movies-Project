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
    private ListItemClickListener listener;

    //Interface
    public interface ListItemClickListener {

        void onListItemClick(Trailer trailer);
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
        Picasso.get().load("http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg").into(holder.img_View_trailer);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public void setTrailers(List<Trailer> trailers){
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public void setListener(ListItemClickListener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_View_trailer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_View_trailer = itemView.findViewById(R.id.img_View_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer videoClick = trailers.get(adapterPosition);
            listener.onListItemClick(videoClick);
        }
    }
}
