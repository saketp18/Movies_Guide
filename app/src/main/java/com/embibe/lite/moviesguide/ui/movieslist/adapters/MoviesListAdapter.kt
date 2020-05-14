package com.embibe.lite.moviesguide.ui.movieslist.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.embibe.lite.moviesguide.data.models.MoviesResult
import com.embibe.lite.moviesguide.ui.movieslist.viewholders.MoviesItemViewHolder

class MoviesListAdapter(private val rVItemClickListener: RVItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var moviesResult: List<MoviesResult>
    private lateinit var searchMoviesResult: List<MoviesResult>
    private var isSearch = false
    private var isDetails = false

    fun setData(movieResults: List<MoviesResult>) {
        isSearch = false
        isDetails = true
        this.moviesResult = movieResults
        notifyDataSetChanged()
    }

    fun setSearchData(moviesResults: List<MoviesResult>) {
        isSearch = true
        isDetails = false
        searchMoviesResult = moviesResults
        notifyDataSetChanged()
    }

    fun clearSearch() {
        isSearch = false
        isDetails = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MoviesItemViewHolder.create(parent, rVItemClickListener)
    }

    override fun getItemCount(): Int {
        return when {
            isDetails -> {
                moviesResult.size
            }
            isSearch -> {
                searchMoviesResult.size
            }
            else -> {
                0
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MoviesItemViewHolder) {
            if (isDetails) {
                holder.bind(moviesResult[position])
            } else if(isSearch){
                holder.bind(searchMoviesResult[position])
            }
        }
    }

    interface RVItemClickListener{
        fun onBookMarkAdded(position: Int)
    }
}
