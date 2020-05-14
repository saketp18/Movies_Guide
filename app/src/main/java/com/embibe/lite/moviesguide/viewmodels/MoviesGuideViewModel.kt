package com.embibe.lite.moviesguide.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.embibe.lite.moviesguide.data.Repository
import com.embibe.lite.moviesguide.data.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


class MoviesGuideViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var page = 1
    var searchPage = 1
    var isLastPage = false
    var isSearchLastPage = false
    var isSearchNewQuery = false
    var searchQuery = ""
    var state = STATE.DETAILS
    val movieResults: LiveData<ResponseState>
        get() = _moviesResult
    val searchMovieResults: LiveData<ResponseState>
        get() = _searchMovieResults

    private val _searchMovieResults = MutableLiveData<ResponseState>()
    private val _moviesResult = MutableLiveData<ResponseState>()

    fun getMoviesPlayingNow() = viewModelScope.launch(Dispatchers.IO) {
        state = STATE.DETAILS
        try {
            val response = repository.getMoviesPlayingNow(page)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if(it.totalPages == page) {
                            isLastPage = true
                        }
                        _moviesResult.value = ResponseState.Success(it)
                    }
                } else {
                    _moviesResult.value =
                        ResponseState.Fail(response.errorBody()?.toString(), response.code())
                }
            }
        } catch (exception: IOException) {
            _moviesResult.postValue(ResponseState.Error(exception))
        }
    }

    fun getSearchView(query: String, isNewQuery: Boolean = false){
        state = STATE.SEARCH
        isSearchNewQuery = isNewQuery
        searchQuery = query
        if(isNewQuery) {
            searchPage = 1
        }
        searchQuery(query)
    }

    private fun searchQuery(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = repository.getSearch(query, searchPage)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if(page == it.totalPages) {
                            isSearchLastPage
                        }
                        _searchMovieResults.value = ResponseState.Success(it)
                    }
                } else {
                    _searchMovieResults.value =
                        ResponseState.Fail(response.errorBody()?.toString(), response.code())
                }
            }
        } catch (exception: IOException) {
            _searchMovieResults.postValue(ResponseState.Error(exception))
        }
    }


    fun loadMorePages() {
        if(STATE.DETAILS == state) {
            page++
            getMoviesPlayingNow()
        } else {
            searchPage++
            getSearchView(searchQuery)
        }
    }

    fun isLastPages(): Boolean {
        return if(STATE.DETAILS == state) {
            isLastPage
        } else {
            isSearchLastPage
        }
    }

    fun updateState(stateNew: STATE) {
        state = stateNew
    }

    enum class STATE {
        DETAILS,
        SEARCH
    }
}
