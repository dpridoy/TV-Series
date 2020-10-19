package com.dpridoy.tvseries.responses;

import com.dpridoy.tvseries.models.TVShowDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    @Expose
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
