package io.chefbook.sdk.network.impl.clients.interceptors

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.encryption.HybridCryptor
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.images.ImageUtils
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.crypto.AEADBadTagException

class EncryptedImageInterceptor(
  private val encryptedVaultRepository: EncryptedVaultRepository,
  private val recipeEncryptionRepository: RecipeEncryptionRepository,
  private val dispatchers: AppDispatchers,
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response = runBlocking(dispatchers.io) {
    val response = chain.proceed(chain.request())

    val data = response.body?.bytes()
    if (data == null || data.isEmpty() || ImageUtils.isImage(data)) return@runBlocking makeResponseWithBody(
      response,
      data
    )

    val url = chain.request().url
    val recipeId = getRecipeId(url) ?: return@runBlocking makeResponseWithBody(response, data)
    val vaultKey = (encryptedVaultRepository.getVaultPrivateKey().getOrNull()
      ?: return@runBlocking makeResponseWithBody(response, data))
    val recipeKey = (recipeEncryptionRepository.getRecipeKey(recipeId, vaultKey).getOrNull()
      ?: return@runBlocking makeResponseWithBody(response, data))
    val decryptedData = try {
      HybridCryptor.decryptDataBySymmetricKey(data, recipeKey)
    } catch (e: AEADBadTagException) {
      Logger.e("Unable to decrypt encrypted image $url")
      return@runBlocking makeResponseWithBody(response, data)
    }
    Logger.i("Encrypted image $url successfully decrypted")

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

  private fun getRecipeId(url: HttpUrl): String? {
    val pathSegments = url.pathSegments
    val recipesSegmentIndex = pathSegments.indexOf(recipesSegment)
    return pathSegments.getOrNull(recipesSegmentIndex + 1)

  }

  companion object {
    private const val recipesSegment = "recipes"
  }
}
