package com.embibe.lite.moviesguide.di.modules

import com.embibe.lite.moviesguide.ui.movieslist.MovieGuideActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Scoping of viewmodel w.r.t to there activities. Creating a sub-component.
 */
@Module
abstract class ActivityViewModule {

    @ContributesAndroidInjector(modules = [MoviesGuideViewModelModule::class])
    abstract fun injectMoviesGuide(): MovieGuideActivity
}
