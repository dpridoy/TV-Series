package com.dpridoy.tvseries.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.databinding.ItemTvShowBinding;
import com.dpridoy.tvseries.listeners.TVShowsListener;
import com.dpridoy.tvseries.listeners.WatchlistListener;
import com.dpridoy.tvseries.models.TVShows;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder>{

    private List<TVShows> tvShows;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<TVShows> tvShows, WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        ItemTvShowBinding tvShowBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.item_tv_show,parent,false
        );
        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder{

        private ItemTvShowBinding itemTvShowBinding;

        public TVShowViewHolder(ItemTvShowBinding itemTvShowBinding){
            super(itemTvShowBinding.getRoot());
            this.itemTvShowBinding=itemTvShowBinding;
        }

        public void bindTVShow(TVShows tvShows){
            itemTvShowBinding.setTvShow(tvShows);
            itemTvShowBinding.executePendingBindings();
            itemTvShowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchlistListener.onTVShowClicked(tvShows);
                }
            });
            itemTvShowBinding.imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchlistListener.removeTVShowFromWatchlist(tvShows, getAdapterPosition());
                }
            });
            itemTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }

}
