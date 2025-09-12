package io.chefbook.sdk.network.impl.di

import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import io.ktor.client.HttpClient
import org.koin.core.scope.Scope

internal expect fun Scope.createBaseClient():
  HttpClient

internal expect fun Scope.connectivityRepository(): ConnectivityRepository
