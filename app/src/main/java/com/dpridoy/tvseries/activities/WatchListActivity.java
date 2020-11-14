package com.dpridoy.tvseries.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.databinding.ActivityWatchListBinding;
import com.dpridoy.tvseries.viewmodels.WatchlistViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchlistViewModel viewModel;

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
    }

    private void loadWatchlist(){
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable= new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvShows -> {
            activityWatchListBinding.setIsLoading(false);
            Toast.makeText(getApplicationContext(),"Watchlist: "+tvShows.size(),Toast.LENGTH_SHORT).show();
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchlist();
    }
}