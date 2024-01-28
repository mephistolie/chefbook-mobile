package io.chefbook.sdk.encryption.recipe.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.recipe.impl.data.sources.RecipeEncryptionSource

internal interface LocalRecipeEncryptionSource : RecipeEncryptionSource {

  suspend fun clear(): EmptyResult
}
