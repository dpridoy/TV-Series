package com.dpridoy.tvseries.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dpridoy.tvseries.repositories.SearchTVShowRepository;
import com.dpridoy.tvseries.responses.TVShowsResponses;

public class SearchViewModel extends ViewModel {

    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel(){
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TVShowsResponses> searchTVShow(String query,int page){
        return searchTVShowRepository.searchTVShow(query,page);
    }
}
