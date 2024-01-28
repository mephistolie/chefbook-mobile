package io.chefbook.sdk.network.impl.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConnectivityRepositoryImpl(
  context: Context,
) : ConnectivityRepository {

  private val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private val connectivityFlow = callbackFlow {

    object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        super.onAvailable(network)
        launch { send(true) }
      }

      override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        launch { send(false) }
      }

      override fun onLost(network: Network) {
        super.onLost(network)
        launch { send(false) }
      }

      override fun onUnavailable() {
        super.onUnavailable()
        launch { send(false) }
      }
    }.also { callback ->
      val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

      connectivityManager.registerNetworkCallback(networkRequest, callback)

      awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
      }
    }
  }
    .distinctUntilChanged()

  override fun observeConnectivity() = connectivityFlow

  override suspend fun hasActiveConnection() = connectivityFlow.first()
}
