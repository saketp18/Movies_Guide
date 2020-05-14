package com.embibe.lite.moviesguide.di.modules

import com.embibe.lite.moviesguide.ui.movieslist.MovieGuideActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityViewModule {

    @ContributesAndroidInjector(modules = [MoviesGuideViewModelModule::class])
    abstract fun injectMoviesGuide(): MovieGuideActivity
}
