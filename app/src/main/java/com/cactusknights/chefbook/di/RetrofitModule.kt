package com.cactusknights.chefbook.di

import android.content.SharedPreferences
import com.cactusknights.chefbook.common.Constants
import com.cactusknights.chefbook.common.Constants.ACCESS_TOKEN
import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.domain.RecipesProvider
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.source.repository.ChefBookAuthRepository
import com.cactusknights.chefbook.source.remote.ChefBookApi
import com.cactusknights.chefbook.source.repository.ChefBookRecipesRepository
import com.cactusknights.chefbook.source.repository.FirebaseRepositoryImpl
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
    fun httpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun httpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        sharedPreferences: SharedPreferences
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer ${sharedPreferences.getString(ACCESS_TOKEN, "").orEmpty()}"
                    )
                    .build()

                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideChefBookApi(okHttpClient: OkHttpClient): ChefBookApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChefBookApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(api: ChefBookApi, sp: SharedPreferences): AuthProvider {
        return ChefBookAuthRepository(api, sp)
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(api: ChefBookApi, sp: SharedPreferences): RecipesProvider {
        return ChefBookRecipesRepository(api, sp)
    }
}