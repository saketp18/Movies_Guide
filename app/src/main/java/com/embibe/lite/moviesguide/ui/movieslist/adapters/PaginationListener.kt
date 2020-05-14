package com.embibe.lite.moviesguide.ui.movieslist.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var firstVisibleItemPosition = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = layoutManager.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >=
                totalItemCount && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val isLastPage: Boolean
}
