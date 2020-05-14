package com.embibe.lite.moviesguide.ui.movieslist.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.embibe.lite.moviesguide.data.local.entity.MovieEntity
import com.embibe.lite.moviesguide.ui.movieslist.viewholders.MoviesItemViewHolder

class MoviesHorizontalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val bookMarkData = ArrayList<MovieEntity>()

    fun setData(moviesEntity: List<MovieEntity>) {
        bookMarkData.clear()
        bookMarkData.addAll(moviesEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MoviesItemViewHolder.create(parent, null)

    override fun getItemCount(): Int = bookMarkData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MoviesItemViewHolder) {
            holder.bind(bookMarkData[position])
        }
    }
}
