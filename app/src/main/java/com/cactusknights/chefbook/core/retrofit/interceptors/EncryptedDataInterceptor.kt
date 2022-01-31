package com.cactusknights.chefbook.core.retrofit.interceptors

import android.app.Activity
import android.content.Context
import com.cactusknights.chefbook.core.encryption.Cryptor
import com.cactusknights.chefbook.repositories.sync.SyncEncryptionRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewComponent
import dagger.hilt.android.components.ViewWithFragmentComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope.coroutineContext
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class EncryptedDataInterceptor(val context: Activity, val key: SecretKey) : Interceptor {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface CryptorProvider {
        fun cryptor() : Cryptor
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val component = EntryPoints.get(context, CryptorProvider::class.java)
        val response = chain.proceed(chain.request())

        val data = response.body!!.byteStream().readBytes()

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