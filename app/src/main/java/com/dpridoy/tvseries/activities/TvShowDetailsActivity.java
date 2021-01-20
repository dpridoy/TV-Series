package com.dpridoy.tvseries.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.adapter.EpisodesAdapter;
import com.dpridoy.tvseries.adapter.ImageSliderAdapter;
import com.dpridoy.tvseries.databinding.ActivityTvShowDetailsBinding;
import com.dpridoy.tvseries.databinding.LayoutEpisodesBottomSheetBinding;
import com.dpridoy.tvseries.models.TVShows;
import com.dpridoy.tvseries.responses.TVShowDetailsResponse;
import com.dpridoy.tvseries.utils.TempDataHolder;
import com.dpridoy.tvseries.viewmodels.TVShowsDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TvShowDetailsActivity extends AppCompatActivity {

    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TVShowsDetailsViewModel tvShowsDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShows tvShows;
    private Boolean isTVShowAvailableInWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvShowDetailsBinding= DataBindingUtil.setContentView(this,R.layout.activity_tv_show_details);

        doInit();
    }

    private void doInit(){
        tvShowsDetailsViewModel=new ViewModelProvider(this).get(TVShowsDetailsViewModel.class);
        activityTvShowDetailsBinding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShows = (TVShows)getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowsDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShows.getId()))
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvShows->{
            isTVShowAvailableInWatchlist = true;
            activityTvShowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added);
        }));
    }

    private void getTVShowDetails(){
        activityTvShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShows.getId());
        tvShowsDetailsViewModel.getTVShowDetails(tvShowId).observe(this, new Observer<TVShowDetailsResponse>() {
            @Override
            public void onChanged(TVShowDetailsResponse tvShowDetailsResponse) {
                activityTvShowDetailsBinding.setIsLoading(false);
                if (tvShowDetailsResponse.getTvShowDetails() != null){
                    if (tvShowDetailsResponse.getTvShowDetails().getPictures()!=null){
                        loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                    }
                    activityTvShowDetailsBinding.setTvShowImageURL(
                            tvShowDetailsResponse.getTvShowDetails().getImagePath()
                    );
                    activityTvShowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.setDescription(
                            String.valueOf(
                                    HtmlCompat.fromHtml(
                                            tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                            HtmlCompat.FROM_HTML_MODE_LEGACY
                                    )
                            )
                    );
                    activityTvShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (activityTvShowDetailsBinding.textReadMore.getText().toString().equals("Read More")){
                                activityTvShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvShowDetailsBinding.textDescription.setEllipsize(null);
                                activityTvShowDetailsBinding.textReadMore.setText("Read Less");
                            }else {
                                activityTvShowDetailsBinding.textDescription.setMaxLines(4);
                                activityTvShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvShowDetailsBinding.textReadMore.setText("Read More");
                            }
                        }
                    });
                    activityTvShowDetailsBinding.setRating(
                            String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                            )
                    );
                    if(tvShowDetailsResponse.getTvShowDetails().getGenres()!=null){
                        activityTvShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres().get(0));
                    }else {
                        activityTvShowDetailsBinding.setGenre("N/A");
                    }
                    activityTvShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                    activityTvShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.buttonWebsite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        }
                    });
                    activityTvShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                    activityTvShowDetailsBinding.buttonEpisodes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (episodesBottomSheetDialog==null){
                                episodesBottomSheetDialog=new BottomSheetDialog(TvShowDetailsActivity.this);
                                layoutEpisodesBottomSheetBinding=DataBindingUtil.inflate(
                                        LayoutInflater.from(TvShowDetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );
                                episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                        new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                                );
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", tvShows.getName())
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        episodesBottomSheetDialog.dismiss();
                                    }
                                });
                            }
                            // Optional Section Start //

                            FrameLayout frameLayout=episodesBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout!=null){
                                BottomSheetBehavior<View> bottomSheetBehavior=BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }

                            // Optional Section End //

                            episodesBottomSheetDialog.show();
                        }
                    });
                    activityTvShowDetailsBinding.imageWatchlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CompositeDisposable compositeDisposable=new CompositeDisposable();
                            if (isTVShowAvailableInWatchlist){
                                compositeDisposable.add(tvShowsDetailsViewModel.removeTVShowFromWatchlist(tvShows)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(()->{
                                            isTVShowAvailableInWatchlist = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvShowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_watchlist);
                                            Toast.makeText(getApplicationContext(),"Removed from watchlist",Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));
                            }else {
                                compositeDisposable.add(tvShowsDetailsViewModel.addToWatchlist(tvShows)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(()->{
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvShowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added);
                                            Toast.makeText(getApplicationContext(),"Added to watchlist",Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));
                            }

                        }
                    });
                    activityTvShowDetailsBinding.imageWatchlist.setVisibility(View.VISIBLE);
                    loadBasicTVShowDetails();
                }
            }
        });
    }

    private void loadImageSlider(List<String> pictures) {
        activityTvShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(pictures));
        activityTvShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicator(pictures.size());
        activityTvShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicator(int count){
        ImageView[] indicator = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i = 0; i<indicator.length; i++){
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(
                    ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.background_slider_indicator_inactive
                    )
            );
            indicator[i].setLayoutParams(layoutParams);
            activityTvShowDetailsBinding.layoutSliderIndicators.addView(indicator[i]);
        }
        activityTvShowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setupCurrentSliderIndicator(0);
    }

    private void setupCurrentSliderIndicator(int position){
        int childCount = activityTvShowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i=0; i <childCount; i++){
            ImageView imageView = (ImageView)activityTvShowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.background_slider_indicator_active
                        )
                );
            }else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.background_slider_indicator_inactive
                        )
                );
            }
        }
    }

    private void loadBasicTVShowDetails(){
        activityTvShowDetailsBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityTvShowDetailsBinding.setNetworkCountry(
                tvShows.getNetwork() +" ("+
                        tvShows.getCountry()+")"
        );
        activityTvShowDetailsBinding.setStatus(tvShows.getStatus());
        activityTvShowDetailsBinding.setStartedDate(tvShows.getStartDate());
        activityTvShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }
}