package com.dpridoy.tvseries.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dpridoy.tvseries.database.TVShowsDatabase;
import com.dpridoy.tvseries.models.TVShows;

import java.util.List;

import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {

    private TVShowsDatabase tvShowsDatabase;


    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public Flowable<List<TVShows>> loadWatchlist(){
        return tvShowsDatabase.tvShowDao().getWatchlist();
    }
}
