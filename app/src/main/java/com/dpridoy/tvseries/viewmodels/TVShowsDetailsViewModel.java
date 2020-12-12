package com.dpridoy.tvseries.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dpridoy.tvseries.database.TVShowsDatabase;
import com.dpridoy.tvseries.models.TVShows;
import com.dpridoy.tvseries.repositories.TVShowsDetailsRepository;
import com.dpridoy.tvseries.responses.TVShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowsDetailsViewModel extends AndroidViewModel {

    private TVShowsDetailsRepository tvShowsDetailsRepository;
    private TVShowsDatabase tvShowsDatabase;

    public TVShowsDetailsViewModel(@NonNull Application application){
        super(application);
        tvShowsDetailsRepository=new TVShowsDetailsRepository();
        tvShowsDatabase=TVShowsDatabase.getTvShowsDatabase(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowsDetailsRepository.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchlist(TVShows tvShows){
        return tvShowsDatabase.tvShowDao().addToWatchlist(tvShows);
    }

    public Flowable<TVShows> getTVShowFromWatchlist(String tvShowId){
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }

    public Completable removeTVShowFromWatchlist(TVShows tvShows){
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvShows);
    }
}
