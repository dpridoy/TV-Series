package com.dpridoy.tvseries.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dpridoy.tvseries.network.Api;
import com.dpridoy.tvseries.network.RetrofitClient;
import com.dpridoy.tvseries.responses.TVShowDetailsResponse;
import com.dpridoy.tvseries.responses.TVShowsResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsDetailsRepository {

    private Api api;

    public TVShowsDetailsRepository(){
        api= RetrofitClient.getRetrofit().create(Api.class);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        MutableLiveData<TVShowDetailsResponse> data=new MutableLiveData<>();
        api.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowDetailsResponse> call,@NonNull Response<TVShowDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowDetailsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
