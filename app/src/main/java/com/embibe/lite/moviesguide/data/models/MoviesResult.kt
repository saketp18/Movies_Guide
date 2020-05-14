package com.embibe.lite.moviesguide.data.models

import com.google.gson.annotations.SerializedName

data class MoviesResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String
)
