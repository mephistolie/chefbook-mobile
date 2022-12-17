package com.mysty.chefbook.api.common.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.NetworkHandler
import com.mysty.chefbook.api.common.network.adapter.ResultAdapterFactory
import com.mysty.chefbook.api.common.network.interceptors.AuthInterceptor
import com.mysty.chefbook.api.common.network.interceptors.EncryptedImageInterceptor
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

    singleOf(::createEncryptedImageOkHttpClient) { named(Qualifiers.ENCRYPTED_IMAGE) }

    singleOf(::createAuthorizedOkHttpClient) { named(Qualifiers.AUTHORIZED) }
    single(named(Qualifiers.AUTHORIZED)) { createAuthorizedRetrofit(get(named(Qualifiers.AUTHORIZED))) }

    singleOf(::NetworkHandler) bind INetworkHandler::class
}

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

private fun createEncryptedImageOkHttpClient(
    encryptedImageInterceptor: EncryptedImageInterceptor,
) = OkHttpClient.Builder()
    .addInterceptor(encryptedImageInterceptor)
    .build()

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
