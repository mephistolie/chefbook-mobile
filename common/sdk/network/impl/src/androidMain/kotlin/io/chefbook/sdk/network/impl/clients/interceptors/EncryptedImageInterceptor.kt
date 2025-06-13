package io.chefbook.sdk.network.impl.clients.interceptors

import io.chefbook.libs.crypto.encryption.HybridCryptor
import io.chefbook.libs.crypto.encryption.generateIV
import io.chefbook.libs.crypto.encryption.models.SymmetricCipherData
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.images.ImageUtils
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class EncryptedImageInterceptor(
  private val encryptedVaultRepository: EncryptedVaultRepository,
  private val recipeEncryptionRepository: RecipeEncryptionRepository,
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())

    val data = response.body?.bytes()
    if (data == null || data.isEmpty() || ImageUtils.isImage(data)) return makeResponseWithBody(
      response,
      data
    )

    val url = chain.request().url
    val recipeId = getRecipeId(url) ?: return makeResponseWithBody(response, data)

    return runBlocking {
        val vaultKey = (encryptedVaultRepository.getVaultPrivateKey().getOrNull()
          ?: return@runBlocking makeResponseWithBody(response, data))
        val recipeKey = (recipeEncryptionRepository.getRecipeKey(recipeId, vaultKey).getOrNull()
          ?: return@runBlocking makeResponseWithBody(response, data))
      val decryptedData = try {
        HybridCryptor.decryptBySymmetricKey(
          cipherData = SymmetricCipherData(
            combinedData = data,
            iv = HybridCryptor.generateIV(url.toString()),
          ),
          key = recipeKey,
        )
      } catch (e: Exception) {
        Logger.e { "Unable to decrypt encrypted image $url" }
        return@runBlocking makeResponseWithBody(response, data)
      }
      Logger.i { "Encrypted image $url successfully decrypted" }

      makeResponseWithBody(response, decryptedData)
    }
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