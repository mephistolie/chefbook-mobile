package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.NetworkHandler
import com.cactusknights.chefbook.data.network.adapter.ResultAdapterFactory
import com.cactusknights.chefbook.data.network.api.AuthApi
import com.cactusknights.chefbook.data.network.api.CategoryApi
import com.cactusknights.chefbook.data.network.api.EncryptionApi
import com.cactusknights.chefbook.data.network.api.FileApi
import com.cactusknights.chefbook.data.network.api.ProfileApi
import com.cactusknights.chefbook.data.network.api.RecipeApi
import com.cactusknights.chefbook.data.network.api.ShoppingListApi
import com.cactusknights.chefbook.data.network.interceptors.AuthInterceptor
import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        const val API_URL = "https://api.chefbook.space/"

        val jsonType = "application/json".toMediaType()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    @BaseRetrofit
    fun provideBaseRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(Json.asConverterFactory(jsonType))
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(
        @BaseRetrofit
        retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        session: ISessionRepo,
    ): AuthInterceptor =
        AuthInterceptor(session)

    @Provides
    @Singleton
    fun provideAuthorizedHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    @AuthorizedRetrofit
    fun provideAuthorizedRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(jsonType))
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideCommonApi(
        @AuthorizedRetrofit
        retrofit: Retrofit
    ): FileApi = retrofit.create(FileApi::class.java)

    @Provides
    @Singleton
    fun provideUsersApi(
        @AuthorizedRetrofit
        retrofit: Retrofit
    ): ProfileApi = retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideEncryptionApi(
        @AuthorizedRetrofit
        retrofit: Retrofit
    ): EncryptionApi =
        retrofit.create(EncryptionApi::class.java)

    @Provides
    @Singleton
    fun provideRecipesApi(
        @AuthorizedRetrofit
        retrofit: Retrofit
    ): RecipeApi =
        retrofit.create(RecipeApi::class.java)

    @Provides
    @Singleton
    fun provideCategoriesApi(
        @AuthorizedRetrofit
        retrofit: Retrofit
    ): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideShoppingListApi(
        @AuthorizedRetrofit
        retrofit: Retrofit
    ): ShoppingListApi =
        retrofit.create(ShoppingListApi::class.java)

}

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindModule {

    @Binds
    fun bindNetworkHandler(handler: NetworkHandler): INetworkHandler

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthorizedRetrofit
