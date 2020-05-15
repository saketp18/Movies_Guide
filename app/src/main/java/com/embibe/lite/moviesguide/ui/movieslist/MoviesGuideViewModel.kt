package com.embibe.lite.moviesguide.ui.movieslist

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.embibe.lite.moviesguide.data.Repository
import com.embibe.lite.moviesguide.data.ResponseState
import com.embibe.lite.moviesguide.data.local.entity.MovieEntity
import com.embibe.lite.moviesguide.data.models.MoviesResult
import com.embibe.lite.moviesguide.utils.MovieState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.ArrayList

class MoviesGuideViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var page = 1
    private var searchPage = 1
    private var isLastPage = false
    private var isSearchLastPage = false
    private var searchQuery = ""
    var state = MovieState.DETAILS
    val movieResults: LiveData<ResponseState>
        get() = _moviesResult
    val searchMovieResults: LiveData<ResponseState>
        get() = _searchMovieResults
    private val _searchMovieResults = MutableLiveData<ResponseState>()
    private val _moviesResult = MutableLiveData<ResponseState>()
    private val _moviesList = ArrayList<MoviesResult>()
    private val _searchResultList = ArrayList<MoviesResult>()

    fun getMoviesPlayingNow() = viewModelScope.launch(Dispatchers.IO) {
        state = MovieState.DETAILS
        try {
            val response = repository.getMoviesPlayingNow(page)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.totalPages == page) {
                            isLastPage = true
                        }
                        _moviesList.addAll(it.results)
                        _moviesResult.value = ResponseState.Success(_moviesList)
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

    fun getMovieSearchResult(query: String, isNewQuery: Boolean = false) {
        if (isNewQuery) {
            searchPage = 1
            _searchResultList.clear()
        }
        state = MovieState.SEARCH
        searchQuery = query
        searchQuery(query)
    }

    private fun searchQuery(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = repository.getSearch(query, searchPage)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (page == it.totalPages) {
                            isSearchLastPage
                        }
                        _searchResultList.addAll(it.results)
                        _searchMovieResults.value = ResponseState.Success(_searchResultList)
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

    fun saveMovie(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        val moviesResult = if(state == MovieState.DETAILS) {
            _moviesList[position]
        } else {
            _searchResultList[position]
        }
        val movieEntity = MovieEntity(0, moviesResult.title, moviesResult.posterPath)
        repository.saveMovie(movieEntity)
    }

    private val _bookmarksData = repository.getMoviesFromBookmark()

    val bookmarksData: LiveData<List<MovieEntity>>
        get() = _bookmarksData

    fun loadMorePages() {
        if (MovieState.DETAILS == state) {
            page++
            getMoviesPlayingNow()
        } else {
            searchPage++
            getMovieSearchResult(searchQuery)
        }
    }

    fun isLastPages(): Boolean {
        return if (MovieState.DETAILS == state) {
            isLastPage
        } else {
            isSearchLastPage
        }
    }

    fun isMovieListEmpty(): Boolean {
        return _moviesList.size == 0
    }

    fun notifyMoviesData() {
        if(state == MovieState.DETAILS) {
            _moviesResult.value = ResponseState.Success(_moviesList)
        }
    }

    fun updateState(stateNew: MovieState) {
        state = stateNew
        if (stateNew == MovieState.DETAILS) {
            _searchResultList.clear()
        }
    }
}
