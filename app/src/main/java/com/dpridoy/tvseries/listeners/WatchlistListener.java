package com.dpridoy.tvseries.listeners;

import com.dpridoy.tvseries.models.TVShows;

public interface WatchlistListener {

    void onTVShowClicked(TVShows tvShows);

    void removeTVShowFromWatchlist(TVShows tvShows, int position);
}
