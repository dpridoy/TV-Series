package com.dpridoy.tvseries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dpridoy.tvseries.models.TVShows;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TVShowDao {

    @Query("SELECT * FROM tvShows")
    Flowable<List<TVShows>> getWatchlist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchlist(TVShows tvShows);

    @Delete
    void removeFromWatchlist(TVShows tvShows);
}