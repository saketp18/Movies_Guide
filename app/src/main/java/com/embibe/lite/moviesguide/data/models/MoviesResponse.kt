package com.embibe.lite.moviesguide.data.models

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MoviesResult>,
    @SerializedName("total_pages")
    val totalPages: Int
)
