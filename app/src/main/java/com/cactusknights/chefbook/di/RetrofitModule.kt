package com.cactusknights.chefbook.di

import android.content.SharedPreferences
import com.cactusknights.chefbook.base.Constants
import com.cactusknights.chefbook.repositories.remote.datasources.AuthInterceptor
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.api.SessionApi
import com.cactusknights.chefbook.repositories.sync.SyncSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideSessionApi(): SessionApi =
        Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SessionApi::class.java)

    @Provides
    @Singleton
    fun provideAuthInterceptor(authApi: SessionApi, sp: SharedPreferences, sr: SyncSettingsRepository): AuthInterceptor =
        AuthInterceptor(authApi, sp, sr)

    @Provides
    @Singleton
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideChefBookApi(okHttpClient: OkHttpClient): ChefBookApi =
        Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChefBookApi::class.java)
}