package com.dpridoy.tvseries.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.adapter.TVShowsAdapter;
import com.dpridoy.tvseries.databinding.ActivityMainBinding;
import com.dpridoy.tvseries.models.TVShows;
import com.dpridoy.tvseries.responses.TVShowsResponses;
import com.dpridoy.tvseries.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MostPopularTVShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TVShows> tvShows=new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization(){
        activityMainBinding.tvShowsRecyclerView.setHasFixedSize(true);
        viewModel=new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter=new TVShowsAdapter(tvShows);
        activityMainBinding.tvShowsRecyclerView.setAdapter(tvShowsAdapter);
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows(){
        activityMainBinding.setIsLoading(true);
        viewModel.getMostPopularTVShows(0).observe(this, new Observer<TVShowsResponses>() {
            @Override
            public void onChanged(TVShowsResponses tvShowsResponses) {
                activityMainBinding.setIsLoading(false);
                if (tvShowsResponses != null){
                    if (tvShowsResponses.getTvShows()!=null){
                        tvShows.addAll(tvShowsResponses.getTvShows());
                        tvShowsAdapter.notifyDataSetChanged();
                    }
                }
                Log.e("Pages: ",tvShowsResponses.getPages().toString());
            }
        });
    }
}