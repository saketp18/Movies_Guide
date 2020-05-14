package com.embibe.lite.moviesguide.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.embibe.lite.moviesguide.data.local.entity.MovieEntity


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun loadPopularArticles(): LiveData<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticles(articleEntities: List<MovieEntity>)
}
