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
import com.cactusknights.chefbook.data.network.interceptors.EncryptedImageInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mysty.chefbook.core.di.Qualifiers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

private const val API_URL = "https://api.chefbook.space/"

private val jsonType = "application/json".toMediaType()

val networkModule = module {
    singleOf(::loggingInterceptor)
    singleOf(::AuthInterceptor)
    singleOf(::EncryptedImageInterceptor)

    singleOf(::baseRetrofit) { named(Qualifiers.ANONYMOUS) }
    single { createAuthApi(get(named(Qualifiers.ANONYMOUS))) }

    singleOf(::createAuthorizedOkHttpClient) { named(Qualifiers.AUTHORIZED) }
    single(named(Qualifiers.AUTHORIZED)) { createAuthorizedRetrofit(get(named(Qualifiers.AUTHORIZED))) }

    single { createFileApi(get(named(Qualifiers.AUTHORIZED))) }
    single { createProfileApi(get(named(Qualifiers.AUTHORIZED))) }
    single { createEncryptionApi(get(named(Qualifiers.AUTHORIZED))) }
    single { createRecipeApi(get(named(Qualifiers.AUTHORIZED))) }
    single { createCategoryApi(get(named(Qualifiers.AUTHORIZED))) }
    single { createShoppingListApi(get(named(Qualifiers.AUTHORIZED))) }

    singleOf(::NetworkHandler) bind INetworkHandler::class
}

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

@OptIn(ExperimentalSerializationApi::class)
private val baseRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl(API_URL)
    .addConverterFactory(Json.asConverterFactory(jsonType))
    .addCallAdapterFactory(ResultAdapterFactory())
    .build()

private fun createAuthorizedOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    authInterceptor: AuthInterceptor,
) = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(loggingInterceptor)
    .build()

@OptIn(ExperimentalSerializationApi::class)
private fun createAuthorizedRetrofit(
    okHttpClient: OkHttpClient,
): Retrofit =
    Retrofit.Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory(jsonType))
        .addCallAdapterFactory(ResultAdapterFactory())
        .build()

private fun createAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)
private fun createFileApi(retrofit: Retrofit) = retrofit.create(FileApi::class.java)
private fun createProfileApi(retrofit: Retrofit) = retrofit.create(ProfileApi::class.java)
private fun createEncryptionApi(retrofit: Retrofit) = retrofit.create(EncryptionApi::class.java)
private fun createRecipeApi(retrofit: Retrofit) = retrofit.create(RecipeApi::class.java)
private fun createCategoryApi(retrofit: Retrofit) = retrofit.create(CategoryApi::class.java)
private fun createShoppingListApi(retrofit: Retrofit) = retrofit.create(ShoppingListApi::class.java)
