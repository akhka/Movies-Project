package com.example.mvvmtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

}
