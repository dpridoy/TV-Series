package com.dpridoy.tvseries.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dpridoy.tvseries.repositories.MostPopularTVShowsRepository;
import com.dpridoy.tvseries.responses.TVShowsResponses;

public class MostPopularTVShowsViewModel extends ViewModel {

    private MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowsViewModel(){
        mostPopularTVShowsRepository=new MostPopularTVShowsRepository();
    }

    public LiveData<TVShowsResponses> getMostPopularTVShows(int page){
        return mostPopularTVShowsRepository.getMostPopularTVShows(page);
    }
}
