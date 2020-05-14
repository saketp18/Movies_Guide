package com.embibe.lite.moviesguide.data.remote

import com.embibe.lite.moviesguide.data.models.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    @GET(NOW_PLAYING)
    suspend fun getMoviesPlayingNow(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MoviesResponse>

    @GET(SEARCH)
    suspend fun getSearch(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<MoviesResponse>

    companion object {
        const val NOW_PLAYING = "/3/movie/now_playing/"
        const val SEARCH = "/3/search/movie"
    }
}
