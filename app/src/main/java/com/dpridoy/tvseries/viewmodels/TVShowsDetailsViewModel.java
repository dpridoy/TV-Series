package com.dpridoy.tvseries.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.dpridoy.tvseries.repositories.TVShowsDetailsRepository;
import com.dpridoy.tvseries.responses.TVShowDetailsResponse;

public class TVShowsDetailsViewModel extends ViewModel {

    private TVShowsDetailsRepository tvShowsDetailsRepository;

    public TVShowsDetailsViewModel(){
        tvShowsDetailsRepository=new TVShowsDetailsRepository();
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowsDetailsRepository.getTVShowDetails(tvShowId);
    }
}
