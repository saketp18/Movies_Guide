package com.embibe.lite.moviesguide.di.modules

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.embibe.lite.moviesguide.BuildConfig
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

/**
 * This is can be made more modular by keeping one module as network module and second one as
 * database module
 */
@Module
object AppModule {

    @JvmStatic
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

    @JvmStatic
    @Provides
    @Singleton
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(application, MovieDataBase::class.java, "db-name").build()

    @JvmStatic
    @Singleton
    @Provides
    fun providesMovieDao(movieDataBase: MovieDataBase): MovieDao {
        return movieDataBase.movieDao()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun moviesService(retrofit: Retrofit): MoviesService {
        return retrofit.create<MoviesService>(MoviesService::class.java)
    }
}
