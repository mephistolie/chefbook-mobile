package io.chefbook.sdk.network.impl.clients.configuration

import android.annotation.SuppressLint
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomX509TrustManager")
internal val developTrustManager = object : X509TrustManager {
  override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) = Unit
  override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) = Unit
  override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOf()
}
