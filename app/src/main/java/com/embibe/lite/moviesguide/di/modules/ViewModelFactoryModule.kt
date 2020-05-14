package com.embibe.lite.moviesguide.di.modules

import androidx.lifecycle.ViewModelProvider
import com.embibe.lite.moviesguide.di.viewmodelfactory.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}
