package com.dpridoy.tvseries.network;

import com.dpridoy.tvseries.responses.TVShowDetailsResponse;
import com.dpridoy.tvseries.responses.TVShowsResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("most-popular")
    Call<TVShowsResponses> getMostPopularTVShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getTVShowDetails(@Query("q") String tvShowId);
}
