package com.cactusknights.chefbook.core.retrofit.interceptors

import android.app.Activity
import com.cactusknights.chefbook.core.encryption.Cryptor
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.crypto.SecretKey

class EncryptedDataInterceptor(val context: Activity, val key: SecretKey) : Interceptor {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface CryptorProvider {
        fun cryptor() : Cryptor
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