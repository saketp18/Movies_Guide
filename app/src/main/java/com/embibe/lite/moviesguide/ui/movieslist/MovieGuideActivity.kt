package com.embibe.lite.moviesguide.ui.movieslist

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.embibe.lite.moviesguide.R
import com.embibe.lite.moviesguide.data.ResponseState
import com.embibe.lite.moviesguide.data.models.MoviesResult
import com.embibe.lite.moviesguide.databinding.ActivityMovieGuideBinding
import com.embibe.lite.moviesguide.di.viewmodelfactory.ViewModelProviderFactory
import com.embibe.lite.moviesguide.ui.movieslist.adapters.MoviesHorizontalAdapter
import com.embibe.lite.moviesguide.ui.movieslist.adapters.MoviesVerticalListAdapter
import com.embibe.lite.moviesguide.ui.movieslist.adapters.PaginationListener
import com.embibe.lite.moviesguide.utils.MovieState
import com.embibe.lite.moviesguide.viewmodels.MoviesGuideViewModel
import com.embibe.lite.moviesguide.utils.connectivity.base.ConnectivityProvider
import dagger.android.AndroidInjection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Landing Activity for movie details.
 */
class MovieGuideActivity : AppCompatActivity(), MoviesVerticalListAdapter.RVItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    lateinit var moviesGuideViewModel: MoviesGuideViewModel
    private val moviesListAdapter = MoviesVerticalListAdapter(this)
    private lateinit var activityMovieGuideBinding: ActivityMovieGuideBinding
    private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        activityMovieGuideBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_movie_guide)
        moviesGuideViewModel =
            ViewModelProvider(this, viewModelFactory).get(MoviesGuideViewModel::class.java)
        setupMovieList()
        setupResultObservers()
        handleSearchListeners()
        if (moviesGuideViewModel.isMovieListEmpty()) {
            fetchMoviesPlayingNow()
        } else {
            moviesGuideViewModel.notifyMoviesData()
        }
    }

    /**
     * Setup your recyclerview with animation whoooohhoooo and add adapter to it.
     */
    private fun setupMovieList() {
        val verticalViewManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        activityMovieGuideBinding.movieDetailsList.apply {
            adapter = moviesListAdapter
            layoutManager = verticalViewManager
        }
        setupRecyclerAnimations()
        setupPaginationListener(verticalViewManager)
    }

    /**
     * Setup your recyclerview for pagination. This logic works on the basis of scroll
     * listener changes, and extension of it can be using paging library. But due to being a
     * small project or assignment its an overkill or over-engineered.
     */
    private fun setupPaginationListener(layoutManager: LinearLayoutManager) {
        activityMovieGuideBinding.movieDetailsList.addOnScrollListener(object :
            PaginationListener(layoutManager) {

            override fun loadMoreItems() = moviesGuideViewModel.loadMorePages()

            override val isLastPage: Boolean
                get() = moviesGuideViewModel.isLastPages()
        })
    }

    /**
     * Animation to load first time.
     */
    private fun setupRecyclerAnimations() {
        val controller = AnimationUtils
            .loadLayoutAnimation(this, R.anim.recycler_animation)
        activityMovieGuideBinding.movieDetailsList.layoutAnimation = controller
    }

    private fun fetchMoviesPlayingNow() {
        if (provider.getNetworkState().hasInternet()) {
            moviesGuideViewModel.getMoviesPlayingNow()
        } else {
            showNoInternetConnectivity()
        }
    }

    private fun getSearchQueries(query: String) {
        if (provider.getNetworkState().hasInternet()) {
            moviesGuideViewModel.getMovieSearchResult(query, true)
        } else {
            showNoInternetConnectivity()
        }
    }

    /**
     * Live-data based on different response received from server based on user state.
     */
    private fun setupResultObservers() {
        moviesGuideViewModel.apply {
            movieResults.observe(this@MovieGuideActivity, Observer {
                it.handleResponse(it,
                    { updateUi(it as ResponseState.Success) },
                    { someThingWentWrong(it as ResponseState.Fail) },
                    { showNoInternetConnectivity() })
            })

            searchMovieResults.observe(this@MovieGuideActivity, Observer {
                it.handleResponse(it,
                    { updateSearchUi(it as ResponseState.Success) },
                    { someThingWentWrong(it as ResponseState.Fail) },
                    { showNoInternetConnectivity() })
            })
            bookmarksData.observe(this@MovieGuideActivity, Observer {
                it?.let {
                    if (it.isNotEmpty()) {
                        val horizontalAdapter = MoviesHorizontalAdapter()
                        val horizontalLayoutManager = LinearLayoutManager(
                            this@MovieGuideActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        activityMovieGuideBinding.movieBookmarksDetailsList.apply {
                            visibility = View.VISIBLE
                            adapter = horizontalAdapter
                            layoutManager = horizontalLayoutManager
                        }
                        horizontalAdapter.setData(it)
                    }
                }
            })
        }
    }

    private fun updateUi(responseState: ResponseState.Success) {
        moviesListAdapter.setData(responseState.data as List<MoviesResult>)
    }

    private fun updateSearchUi(responseState: ResponseState.Success) {
        moviesListAdapter.setSearchData(responseState.data as List<MoviesResult>)
    }

    private fun someThingWentWrong(responseState: ResponseState.Fail) {
        showErrorLayout()
        activityMovieGuideBinding.errorLayout.apply {
            errorTitle.text =
                responseState.message ?: resources.getString(R.string.no_internet_description)
            closeError.setOnClickListener {
                closeErrorLayout()
                fetchMoviesPlayingNow()
            }
        }
    }

    private fun showNoInternetConnectivity() {
        showErrorLayout()
        activityMovieGuideBinding.errorLayout.apply {
            errorTitle.text = resources.getString(R.string.no_internet_description)
            closeError.setOnClickListener {
                closeErrorLayout()
                fetchMoviesPlayingNow()
            }
        }
    }

    private fun showErrorLayout() {
        activityMovieGuideBinding.errorLayout.errorParent.visibility = View.VISIBLE
    }

    private fun closeErrorLayout() {
        activityMovieGuideBinding.errorLayout.errorParent.visibility = View.GONE
    }

    override fun onBookMarkAdded(position: Int) {
        moviesGuideViewModel.saveMovie(position)
        Toast.makeText(this, "Bookmark saved", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("state", moviesGuideViewModel.state.ordinal)
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

    private fun handleSearchListeners() {
        val listener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
            private var searchFor = ""
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText.toString().trim()
                if(searchText.isEmpty()) {
                    moviesListAdapter.clearSearch()
                }
                if (searchText == searchFor) {
                    return true
                }
                searchFor = searchText
                lifecycleScope.launch {
                    delay(300)
                    if (searchText != searchFor)
                        return@launch
                    if (searchText.isEmpty()) {
                        moviesGuideViewModel.updateState(MovieState.DETAILS)
                        moviesListAdapter.clearSearch()
                    } else {
                        getSearchQueries(searchText)
                    }
                }
                return true
            }
        }
        activityMovieGuideBinding.searchText.setOnQueryTextListener(listener)
    }
}
