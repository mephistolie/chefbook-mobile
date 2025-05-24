package io.chefbook.sdk.recipe.book.api.internal.data.models

import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.sdk.collection.api.external.domain.entities.Collection

data class RecipeBookState(
    val recipes: List<RecipeState>,
    val collections: List<Collection>,
    val profiles: Map<String, ProfileInfo>,
    val isEncryptedVaultEnabled: Boolean,
)
