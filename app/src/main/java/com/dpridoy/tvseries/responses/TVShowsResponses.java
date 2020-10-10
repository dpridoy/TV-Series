package com.dpridoy.tvseries.responses;

import com.dpridoy.tvseries.models.TVShows;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowsResponses {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("tv_shows")
    @Expose
    private List<TVShows> tvShows = null;

    public String getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPages() {
        return pages;
    }

    public List<TVShows> getTvShows() {
        return tvShows;
    }
}
