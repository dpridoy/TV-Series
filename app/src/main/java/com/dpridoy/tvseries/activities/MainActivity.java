package com.dpridoy.tvseries.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.responses.TVShowsResponses;
import com.dpridoy.tvseries.viewmodels.MostPopularTVShowsViewModel;

public class MainActivity extends AppCompatActivity {

    private MostPopularTVShowsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel=new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows(){
        viewModel.getMostPopularTVShows(0).observe(this, new Observer<TVShowsResponses>() {
            @Override
            public void onChanged(TVShowsResponses tvShowsResponses) {
                Toast.makeText(getApplicationContext(),"Total Pages: "+tvShowsResponses.getPages(),Toast.LENGTH_SHORT).show();
                Log.e("Pages: ",tvShowsResponses.getPages().toString());
            }
        });
    }
}