package com.cactusknights.chefbook.di

import android.content.SharedPreferences
import com.cactusknights.chefbook.ChefBookRepository
import com.cactusknights.chefbook.common.Constants
import com.cactusknights.chefbook.common.Constants.ACCESS_TOKEN
import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.domain.RecipesProvider
import com.cactusknights.chefbook.repositories.local.LocalDataSource
import com.cactusknights.chefbook.repositories.local.dao.ChefBookDao
import com.cactusknights.chefbook.repositories.remote.RemoteDataSource
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.ChefBookRecipesRepository
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
    fun provideAuthRepository(api: ChefBookApi, sp: SharedPreferences): RemoteDataSource {
        return RemoteDataSource(api, sp)
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(api: ChefBookApi, sp: SharedPreferences): ChefBookRecipesRepository {
        return ChefBookRecipesRepository(api, sp)
    }

    @Provides
    @Singleton
    fun provideAuthProvider(lp: LocalDataSource, rp: RemoteDataSource, rrp: ChefBookRecipesRepository, sp: SharedPreferences): AuthProvider {
        return ChefBookRepository(lp, rp, rrp, sp)
    }

    @Provides
    @Singleton
    fun provideRecipeProvider(ap: AuthProvider): RecipesProvider {
        return ap as ChefBookRepository
    }
}