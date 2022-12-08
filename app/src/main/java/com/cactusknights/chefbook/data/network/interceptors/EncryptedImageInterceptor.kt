package com.cactusknights.chefbook.data.network.interceptors

import com.cactusknights.chefbook.core.coroutines.AppDispatchers
import com.cactusknights.chefbook.core.encryption.IHybridCryptor
import com.cactusknights.chefbook.core.encryption.ImageValidator
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import javax.crypto.AEADBadTagException
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber

class EncryptedImageInterceptor @Inject constructor(
    private val encryptedVaultRepo: IEncryptedVaultRepo,
    private val recipeEncryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: IHybridCryptor,
    private val dispatchers: AppDispatchers,
) : Interceptor {

    companion object {
        private const val recipesSegment = "recipes"
    }

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking(dispatchers.io) {
        val response = chain.proceed(chain.request())

        val data = response.body?.bytes()
        if (data == null || data.isEmpty() || ImageValidator.isImage(data)) return@runBlocking  makeResponseWithBody(response, data)

        val url = chain.request().url
        val recipeId = getRecipeId(url) ?: return@runBlocking makeResponseWithBody(response, data)
        val userKey = (encryptedVaultRepo.getUserPrivateKey() as? ActionStatus.Success)?.data ?: return@runBlocking makeResponseWithBody(response, data)
        val recipeKey = (recipeEncryptionRepo.getRecipeKey(recipeId, userKey) as? ActionStatus.Success)?.data ?: return@runBlocking makeResponseWithBody(response, data)
        val decryptedData = try {
            cryptor.decryptDataBySymmetricKey(data, recipeKey)
        } catch (e: AEADBadTagException) {
            Timber.e("Unable to decrypt encrypted image $url")
            return@runBlocking makeResponseWithBody(response, data)
        }
        Timber.i("Encrypted image $url successfully decrypted")

        makeResponseWithBody(response, decryptedData)
    }

    private fun makeResponseWithBody(response: Response, body: ByteArray?) =
        Response.Builder()
            .request(response.request)
            .code(response.code)
            .headers(response.headers)
            .protocol(response.protocol)
            .message(response.message)
            .body(body?.toResponseBody())
            .build()

    private fun getRecipeId(url: HttpUrl): Int? {
        val pathSegments = url.pathSegments
        val recipesSegmentIndex = pathSegments.indexOf(recipesSegment)
        return pathSegments.getOrNull(recipesSegmentIndex+1)?.toIntOrNull()

    }
}