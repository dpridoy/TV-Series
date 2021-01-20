package com.dpridoy.tvseries.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.adapter.TVShowsAdapter;
import com.dpridoy.tvseries.databinding.ActivityMainBinding;
import com.dpridoy.tvseries.listeners.TVShowsListener;
import com.dpridoy.tvseries.models.TVShows;
import com.dpridoy.tvseries.responses.TVShowsResponses;
import com.dpridoy.tvseries.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowsListener {

    private MostPopularTVShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TVShows> tvShows=new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage=1;
    private int totalAvailablePages=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization(){
        activityMainBinding.tvShowsRecyclerView.setHasFixedSize(true);
        viewModel=new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter=new TVShowsAdapter(tvShows,this);
        activityMainBinding.tvShowsRecyclerView.setAdapter(tvShowsAdapter);
        activityMainBinding.tvShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowsRecyclerView.canScrollVertically(1)){
                    if (currentPage<=totalAvailablePages){
                        currentPage+=1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        activityMainBinding.imageWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WatchListActivity.class));
            }
        });
        activityMainBinding.imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows(){
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, new Observer<TVShowsResponses>() {
            @Override
            public void onChanged(TVShowsResponses tvShowsResponses) {
                toggleLoading();
                if (tvShowsResponses != null){
                    totalAvailablePages=tvShowsResponses.getPages();
                    if (tvShowsResponses.getTvShows()!=null){
                        int oldCount = tvShows.size();
                        tvShows.addAll(tvShowsResponses.getTvShows());
                        tvShowsAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                    }
                }
            }
        });
    }

    private void toggleLoading(){
        if (currentPage==1){
            if (activityMainBinding.getIsLoading()!=null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else {
                activityMainBinding.setIsLoading(true);
            }
        }else {
            if (activityMainBinding.getIsLoadingMore()!=null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            }else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowCLicked(TVShows tvShows) {
        Intent intent=new Intent(getApplicationContext(),TvShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShows);
        startActivity(intent);
    }
}