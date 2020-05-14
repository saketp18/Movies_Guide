package com.embibe.lite.moviesguide.di.modules

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.embibe.lite.moviesguide.BuildConfig
import com.embibe.lite.moviesguide.MoviesApplication
import com.embibe.lite.moviesguide.data.local.MovieDataBase
import com.embibe.lite.moviesguide.data.local.dao.MovieDao
import com.embibe.lite.moviesguide.data.remote.MoviesService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule() {

    @Singleton
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message: String? ->
                if (BuildConfig.DEBUG) {
                    Log.d("Network", message ?: "Null message")
                }
            })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Application)
            = Room.databaseBuilder(context, MovieDataBase::class.java, "db-name").build()

    @Singleton
    @Provides
    fun providesMovieDao(movieDataBase: MovieDataBase): MovieDao {
        return movieDataBase.movieDao()
    }

    @Provides
    @Singleton
    fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun moviesService(retrofit: Retrofit): MoviesService {
        return retrofit.create<MoviesService>(MoviesService::class.java)
    }
}
