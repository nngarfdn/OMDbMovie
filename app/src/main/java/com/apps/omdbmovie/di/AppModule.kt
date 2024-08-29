package com.apps.omdbmovie.di

import android.content.Context
import androidx.room.Room
import com.apps.omdbmovie.data.local.db.AppDatabase
import com.apps.omdbmovie.data.local.db.OmdbDao
import com.apps.omdbmovie.data.network.ApiService
import com.apps.omdbmovie.data.network.Constants
import com.apps.omdbmovie.data.repository.MovieRepositoryImpl
import com.apps.omdbmovie.domain.usecase.GetMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideOmdbApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOmdbDao(appDatabase: AppDatabase) = appDatabase.omdbDao()

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: ApiService, omdbDao: OmdbDao) =
        MovieRepositoryImpl(apiService, omdbDao)

    @Provides
    @Singleton
    fun provideGetMovieUseCase(movieRepository: MovieRepositoryImpl) =
        GetMovieUseCase(movieRepository)


}