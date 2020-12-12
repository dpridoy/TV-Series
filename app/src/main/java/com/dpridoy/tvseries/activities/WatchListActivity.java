package com.dpridoy.tvseries.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.adapter.WatchlistAdapter;
import com.dpridoy.tvseries.databinding.ActivityWatchListBinding;
import com.dpridoy.tvseries.listeners.WatchlistListener;
import com.dpridoy.tvseries.models.TVShows;
import com.dpridoy.tvseries.utils.TempDataHolder;
import com.dpridoy.tvseries.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShows> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        activityWatchListBinding = DataBindingUtil.setContentView(this,R.layout.activity_watch_list);
        doInit();
    }

    private void doInit(){
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        watchlist = new ArrayList<>();
        loadWatchlist();
    }

    private void loadWatchlist(){
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable= new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvShows -> {
            activityWatchListBinding.setIsLoading(false);
            if (watchlist.size()>0){
                watchlist.clear();
            }
            watchlist.addAll(tvShows);
            watchlistAdapter = new WatchlistAdapter(watchlist, this);
            activityWatchListBinding.watchlistRecyclerView.setAdapter(watchlistAdapter);
            activityWatchListBinding.watchlistRecyclerView.setVisibility(View.VISIBLE);
            compositeDisposable.dispose();
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }

    @Override
    public void onTVShowClicked(TVShows tvShows) {
        Intent intent = new Intent(getApplicationContext(),TvShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShows);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchlist(TVShows tvShows, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(viewModel.removeTVShowFromWatchlist(tvShows)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(()-> {
            watchlist.remove(position);
            watchlistAdapter.notifyItemRemoved(position);
            watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
            compositeDisposableForDelete.dispose();
        }));
    }
}