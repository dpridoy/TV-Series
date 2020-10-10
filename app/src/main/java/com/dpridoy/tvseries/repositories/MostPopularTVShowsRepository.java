package com.dpridoy.tvseries.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dpridoy.tvseries.network.Api;
import com.dpridoy.tvseries.network.RetrofitClient;
import com.dpridoy.tvseries.responses.TVShowsResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowsRepository {

    private Api api;

    public MostPopularTVShowsRepository(){
        api= RetrofitClient.getRetrofit().create(Api.class);
    }

    public LiveData<TVShowsResponses> getMostPopularTVShows(int page){
        MutableLiveData<TVShowsResponses> data=new MutableLiveData<>();
        api.getMostPopularTVShows(page).enqueue(new Callback<TVShowsResponses>() {
            @Override
            public void onResponse(@NonNull Call<TVShowsResponses> call,@NonNull Response<TVShowsResponses> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsResponses> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
