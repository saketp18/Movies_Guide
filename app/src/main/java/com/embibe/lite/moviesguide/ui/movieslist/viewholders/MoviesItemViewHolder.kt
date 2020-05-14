package com.embibe.lite.moviesguide.ui.movieslist.viewholders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.embibe.lite.moviesguide.BuildConfig
import com.embibe.lite.moviesguide.R
import com.embibe.lite.moviesguide.data.models.MoviesResult
import com.embibe.lite.moviesguide.databinding.ItemMovieDetailsBinding
import com.embibe.lite.moviesguide.ui.movieslist.adapters.MoviesListAdapter
import com.embibe.lite.moviesguide.utils.inflateDataBindingLayout

class MoviesItemViewHolder(private var itemMovieDetailsBinding: ItemMovieDetailsBinding) :
    RecyclerView.ViewHolder(itemMovieDetailsBinding.root) {

    companion object {
        fun create(parent: ViewGroup, rvItemClickListener: MoviesListAdapter.RVItemClickListener): MoviesItemViewHolder {
            val vh = MoviesItemViewHolder(parent.inflateDataBindingLayout(R.layout.item_movie_details))
            vh.itemMovieDetailsBinding.bookmark.setOnClickListener {
                rvItemClickListener.onBookMarkAdded(vh.adapterPosition)
            }
            return vh
        }
    }

    fun bind(movies: MoviesResult) {
        itemMovieDetailsBinding.apply {
            movieResult = movies
            Glide.with(poster.context)
                .load(BuildConfig.IMAGE_URL.plus(movies.posterPath))
                .into(poster)
            executePendingBindings()
        }
    }
}
