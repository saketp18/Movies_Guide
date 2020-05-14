package com.embibe.lite.moviesguide.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.embibe.lite.moviesguide.data.local.dao.MovieDao
import com.embibe.lite.moviesguide.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 2)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
