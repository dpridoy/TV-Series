package com.dpridoy.tvseries.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.databinding.ActivityTvShowDetailsBinding;
import com.dpridoy.tvseries.responses.TVShowDetailsResponse;
import com.dpridoy.tvseries.viewmodels.TVShowsDetailsViewModel;

public class TvShowDetailsActivity extends AppCompatActivity {

    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TVShowsDetailsViewModel tvShowsDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvShowDetailsBinding= DataBindingUtil.setContentView(this,R.layout.activity_tv_show_details);

        doInit();
    }

    private void doInit(){
        tvShowsDetailsViewModel=new ViewModelProvider(this).get(TVShowsDetailsViewModel.class);
        getTVShowDetails();
    }

    private void getTVShowDetails(){
        activityTvShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id",-1));
        tvShowsDetailsViewModel.getTVShowDetails(tvShowId).observe(this, new Observer<TVShowDetailsResponse>() {
            @Override
            public void onChanged(TVShowDetailsResponse tvShowDetailsResponse) {
                activityTvShowDetailsBinding.setIsLoading(false);
                Toast.makeText(getApplicationContext(),tvShowDetailsResponse.getTvShowDetails().getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}