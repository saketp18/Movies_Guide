package com.embibe.lite.moviesguide.di.modules

import androidx.lifecycle.ViewModel
import com.embibe.lite.moviesguide.di.viewmodelfactory.ViewModelKey
import com.embibe.lite.moviesguide.ui.movieslist.MoviesGuideViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MoviesGuideViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesGuideViewModel::class)
    abstract fun bindMoviesGuideViewModelModule(viewModel: MoviesGuideViewModel?): ViewModel?
}
