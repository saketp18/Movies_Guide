package com.embibe.lite.moviesguide.ui.movieslist.viewholders

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.embibe.lite.moviesguide.BuildConfig
import com.embibe.lite.moviesguide.R
import com.embibe.lite.moviesguide.data.local.entity.MovieEntity
import com.embibe.lite.moviesguide.data.models.MoviesResult
import com.embibe.lite.moviesguide.databinding.ItemMovieDetailsBinding
import com.embibe.lite.moviesguide.ui.movieslist.adapters.MoviesVerticalListAdapter
import com.embibe.lite.moviesguide.utils.inflateDataBindingLayout

class MoviesItemViewHolder(private var itemMovieDetailsBinding: ItemMovieDetailsBinding) :
    RecyclerView.ViewHolder(itemMovieDetailsBinding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            rvItemClickListener: MoviesVerticalListAdapter.RVItemClickListener?
        ): MoviesItemViewHolder {
            val viewHolder =
                MoviesItemViewHolder(parent.inflateDataBindingLayout(R.layout.item_movie_details))
            viewHolder.itemMovieDetailsBinding.bookmark.setOnClickListener {
                rvItemClickListener?.onBookMarkAdded(viewHolder.adapterPosition)
            }
            return viewHolder
        }
    }

    fun bind(movies: MoviesResult) {
        itemMovieDetailsBinding.apply {
            title.text = movies.title
            Glide.with(poster.context)
                .load(BuildConfig.IMAGE_URL.plus(movies.posterPath))
                .into(poster)
            executePendingBindings()
        }
    }

    fun bind(movies: MovieEntity) {
        itemMovieDetailsBinding.apply {
            title.text = movies.title
            bookmark.visibility = View.GONE
            Glide.with(poster.context)
                .load(BuildConfig.IMAGE_URL.plus(movies.poster))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(poster)
            executePendingBindings()
        }
    }
}
