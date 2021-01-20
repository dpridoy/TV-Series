package com.dpridoy.tvseries.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.adapter.TVShowsAdapter;
import com.dpridoy.tvseries.databinding.ActivitySearchBinding;
import com.dpridoy.tvseries.listeners.TVShowsListener;
import com.dpridoy.tvseries.models.TVShows;
import com.dpridoy.tvseries.responses.TVShowsResponses;
import com.dpridoy.tvseries.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowsListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel viewModel;
    private List<TVShows> tvShows=new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInit();
    }

    private void doInit(){
        activitySearchBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activitySearchBinding.tvShowsRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowsAdapter =new TVShowsAdapter(tvShows, this);
        activitySearchBinding.tvShowsRecyclerView.setAdapter(tvShowsAdapter);
        activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer!=null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    currentPage = 1;
                                    totalAvailablePages = 1;
                                    searchTVShow(s.toString());
                                }
                            });
                        }
                    },1000);
                }else {
                    tvShows.clear();
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });
        activitySearchBinding.tvShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.tvShowsRecyclerView.canScrollVertically(1)){
                    if (!activitySearchBinding.inputSearch.getText().toString().isEmpty()){
                        if (currentPage<totalAvailablePages){
                            currentPage += 1;
                            searchTVShow(activitySearchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        activitySearchBinding.inputSearch.requestFocus();
    }

    private void searchTVShow(String query){
        toggleLoading();
        viewModel.searchTVShow(query, currentPage).observe(this, new Observer<TVShowsResponses>() {
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
            if (activitySearchBinding.getIsLoading()!=null && activitySearchBinding.getIsLoading()){
                activitySearchBinding.setIsLoading(false);
            }else {
                activitySearchBinding.setIsLoading(true);
            }
        }else {
            if (activitySearchBinding.getIsLoadingMore()!=null && activitySearchBinding.getIsLoadingMore()){
                activitySearchBinding.setIsLoadingMore(false);
            }else {
                activitySearchBinding.setIsLoadingMore(true);
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