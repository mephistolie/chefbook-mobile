package com.cactusknights.chefbook.data.network.interceptors

import android.app.Activity
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.crypto.SecretKey
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class EncryptedDataInterceptor(val context: Activity, val key: SecretKey) : Interceptor {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface CryptorProvider {
        fun cryptor() : ICryptor
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val component = EntryPoints.get(context, CryptorProvider::class.java)
        val response = chain.proceed(chain.request())

        val data = response.body?.byteStream()?.readBytes() ?: return response

        val decryptedData = component.cryptor().decryptDataBySymmetricKey(data, key)
        return Response.Builder()
            .request(response.request)
            .code(response.code)
            .headers(response.headers)
            .protocol(response.protocol)
            .message(response.message)
            .body(decryptedData.toResponseBody())
            .build()
    }
}