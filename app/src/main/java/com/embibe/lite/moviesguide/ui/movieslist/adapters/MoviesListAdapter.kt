package com.embibe.lite.moviesguide.ui.movieslist.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.embibe.lite.moviesguide.data.models.MoviesResult
import com.embibe.lite.moviesguide.ui.movieslist.viewholders.MoviesItemViewHolder

class MoviesListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var moviesResult = ArrayList<MoviesResult>()
    private var searchMoviesResult = ArrayList<MoviesResult>()
    private var isSearch = false

    fun setData(movieResults: List<MoviesResult>) {
        isSearch = false
        this.moviesResult.addAll(movieResults)
        notifyDataSetChanged()
    }

    fun setSearchData(moviesResults: List<MoviesResult>, isNewQuery: Boolean) {
        if(isNewQuery) {
            searchMoviesResult.clear()
        }
        isSearch = true
        searchMoviesResult.addAll(moviesResults)
        notifyDataSetChanged()
    }

    fun updateSearch() {
        isSearch = false
        searchMoviesResult.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MoviesItemViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return if(!isSearch) {
            moviesResult.size
        } else {
            searchMoviesResult.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MoviesItemViewHolder) {
            if(!isSearch) {
                holder.bind(moviesResult[position])
            } else {
                holder.bind(searchMoviesResult[position])
            }
        }
    }
}
