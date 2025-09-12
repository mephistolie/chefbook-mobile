package io.chefbook.sdk.network.impl.clients.plugins

import io.chefbook.libs.crypto.encryption.HybridCryptor
import io.chefbook.libs.crypto.encryption.generateIV
import io.chefbook.libs.crypto.encryption.models.SymmetricCipherData
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.images.ImageUtils
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.request
import io.ktor.http.Url
import io.ktor.utils.io.toByteArray

private const val PluginName = "EncryptedImagePlugin"
private const val RecipesSegment = "recipes"

fun EncryptedImagePlugin(
  encryptedVaultRepository: EncryptedVaultRepository,
  recipeEncryptionRepository: RecipeEncryptionRepository,
): ClientPlugin<Unit> {
  return createClientPlugin(PluginName) {
    transformResponseBody { response, content, requestedType ->
      if (requestedType.type != ByteArray::class) return@transformResponseBody content

      val data = content.toByteArray()
      if (data.isEmpty() || ImageUtils.isImage(data)) return@transformResponseBody data

      val url = response.request.url

      val recipeId = getRecipeId(url) ?: return@transformResponseBody data

      val vaultKey = encryptedVaultRepository.getVaultPrivateKey().getOrNull()
        ?: return@transformResponseBody data
      val recipeKey = recipeEncryptionRepository.getRecipeKey(recipeId, vaultKey).getOrNull()
        ?: return@transformResponseBody data

      val decryptedData = try {
        HybridCryptor.decryptBySymmetricKey(
          cipherData = SymmetricCipherData(
            combinedData = data,
            iv = HybridCryptor.generateIV(url.toString()),
          ),
          key = recipeKey,
        )
      } catch (e: Exception) {
        Logger.e(e) { "Unable to decrypt encrypted image $url" }
        return@transformResponseBody data
      }
      Logger.i { "Encrypted image $url successfully decrypted" }

      return@transformResponseBody decryptedData
    }
  }
}

private fun getRecipeId(url: Url): String? {
  val pathSegments = url.rawSegments
  val recipesSegmentIndex = pathSegments.indexOf(RecipesSegment)
  return pathSegments.getOrNull(recipesSegmentIndex + 1)
}
