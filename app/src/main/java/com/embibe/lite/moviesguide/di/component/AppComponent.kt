package com.embibe.lite.moviesguide.di.component

import android.app.Application
import com.embibe.lite.moviesguide.MoviesApplication
import com.embibe.lite.moviesguide.di.modules.ActivityViewModule
import com.embibe.lite.moviesguide.di.modules.ImageModule
import com.embibe.lite.moviesguide.di.modules.NetworkModule
import com.embibe.lite.moviesguide.di.modules.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ActivityViewModule::class,
        ViewModelFactoryModule::class,
        NetworkModule::class,
        ImageModule::class])

interface AppComponent : AndroidInjector<MoviesApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
