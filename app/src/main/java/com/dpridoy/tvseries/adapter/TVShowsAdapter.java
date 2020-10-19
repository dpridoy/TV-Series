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
import com.dpridoy.tvseries.models.TVShows;

import java.util.List;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder>{

    private List<TVShows> tvShows;
    private LayoutInflater layoutInflater;
    private TVShowsListener tvShowsListener;

    public TVShowsAdapter(List<TVShows> tvShows, TVShowsListener tvShowsListener) {
        this.tvShows = tvShows;
        this.tvShowsListener=tvShowsListener;
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
                    tvShowsListener.onTVShowCLicked(tvShows);
                }
            });
        }
    }

}
