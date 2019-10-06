package com.example.mvvmtest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie_table")
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("original_title")
    @Expose
    private String title;
    @SerializedName("poster_path")
    @Expose
    private String posterUrl;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("vote_average")
    @Expose
    private float voteAverage;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private boolean isFavorite = false;

    @Ignore
    public Movie(int id, String title, String posterUrl, String overview, float voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(overview);
        dest.writeValue(posterUrl);
        dest.writeValue(releaseDate);
        dest.writeValue(voteAverage);
    }


    public Movie(){

    }


    @Ignore
    public Movie(Parcel p){
        id = (Integer) p.readValue(Integer.class.getClassLoader());
        title = p.readString();
        overview = p.readString();
        posterUrl = p.readString();
        releaseDate = p.readString();
        voteAverage = p.readFloat();
    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
