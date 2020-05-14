package com.embibe.lite.moviesguide.data

import com.embibe.lite.moviesguide.BuildConfig
import com.embibe.lite.moviesguide.data.local.dao.MovieDao
import com.embibe.lite.moviesguide.data.local.entity.MovieEntity
import com.embibe.lite.moviesguide.data.remote.MoviesService
import javax.inject.Inject

class Repository @Inject constructor(private val moviesService: MoviesService, val moviesDao: MovieDao) {

    suspend fun getMoviesPlayingNow(page: Int) = moviesService.getMoviesPlayingNow(BuildConfig.API_KEY, page)

    suspend fun getSearch(query: String, page: Int) = moviesService.getSearch(BuildConfig.API_KEY, query, page)

    suspend fun saveMovie(movieEntity: MovieEntity) = moviesDao.saveMovie(movieEntity)

    fun getMoviesFromBookmark() = moviesDao.loadMovies()
}
