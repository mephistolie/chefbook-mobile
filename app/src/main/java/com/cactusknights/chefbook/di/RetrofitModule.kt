package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.core.retrofit.interceptors.AuthInterceptor
import com.cactusknights.chefbook.common.retrofit.CommonApi
import com.cactusknights.chefbook.core.datastore.SessionManager
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.data.sources.remote.api.*
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

    companion object {
        const val API_URL = "https://api.chefbook.space/"
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideAuthInterceptor(authApi: SessionApi, session: SessionManager, sr: SettingsManager): AuthInterceptor =
        AuthInterceptor(authApi, session, sr)

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
    fun provideCommonApi(okHttpClient: OkHttpClient): CommonApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommonApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(okHttpClient: OkHttpClient): AuthApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)


    @Provides
    @Singleton
    fun provideSessionApi(): SessionApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SessionApi::class.java)

    @Provides
    @Singleton
    fun provideUsersApi(okHttpClient: OkHttpClient): ProfileApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideEncryptionApi(okHttpClient: OkHttpClient): EncryptionApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EncryptionApi::class.java)

    @Provides
    @Singleton
    fun provideRecipesApi(okHttpClient: OkHttpClient): RecipesApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipesApi::class.java)

    @Provides
    @Singleton
    fun provideCategoriesApi(okHttpClient: OkHttpClient): CategoriesApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoriesApi::class.java)

    @Provides
    @Singleton
    fun provideShoppingListApi(okHttpClient: OkHttpClient): ShoppingListApi =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShoppingListApi::class.java)
}