package io.chefbook.sdk.recipe.crud.impl.data.models

import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.crud.api.internal.data.models.DecryptedRecipeInput
import io.chefbook.sdk.recipe.crud.api.internal.data.models.RecipeProcessedInput
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


internal fun RecipeProcessedInput.toNewRecipe(
  owner: Profile?
) = toRecipe(
  ownerId = owner?.id ?: generateUUID(),
  ownerName = owner?.username,
  ownerAvatar = owner?.avatar,
)

internal fun RecipeProcessedInput.toUpdatedRecipe(
  unmodifiedRecipe: Recipe?
) = toRecipe(
  id = id,
  ownerId = unmodifiedRecipe?.owner?.id ?: generateUUID(),
  ownerName = unmodifiedRecipe?.owner?.name,
  ownerAvatar = unmodifiedRecipe?.owner?.avatar,
  isSaved = unmodifiedRecipe?.isSaved ?: true,
  rating = unmodifiedRecipe?.rating?.index ?: 0F,
  score = unmodifiedRecipe?.rating?.score,
  votes = unmodifiedRecipe?.rating?.votes ?: 0,
  creationTimestamp = unmodifiedRecipe?.creationTimestamp
    ?: Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
  isFavourite = unmodifiedRecipe?.isFavourite ?: false,
)

internal fun RecipeProcessedInput.toRecipe(
  id: String = generateUUID(),
  ownerId: String,
  ownerName: String? = null,
  ownerAvatar: String? = null,
  isOwned: Boolean = true,
  isSaved: Boolean = true,
  rating: Float = 0F,
  score: Int? = null,
  votes: Int = 0,
  creationTimestamp: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
  updateTimestamp: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
  categories: List<Category> = emptyList(),
  isFavourite: Boolean = false,
): Recipe {
  val meta = RecipeMeta(
    id = id,
    owner = ProfileInfo(
      id = ownerId,
      name = ownerName,
      avatar = ownerAvatar,
    ),
    visibility = visibility,
    isEncryptionEnabled = isEncrypted,
    language = language,
    version = version,
    creationTimestamp = creationTimestamp,
    updateTimestamp = updateTimestamp,
    rating = RecipeMeta.Rating(
      index = rating,
      score = score,
      votes = votes,
    )
  )
  return when (this) {
    is RecipeProcessedInput.Decrypted -> DecryptedRecipe(
      info = DecryptedRecipeInfo(
        meta = meta,
        isOwned = isOwned,
        isSaved = isSaved,
        categories = categories,
        isFavourite = isFavourite,
        servings = servings,
        time = time,
        calories = calories,
        name = name,
        preview = (preview as? RecipeInput.Picture.Uploaded)?.path,
      ),
      description = description,
      macronutrients = macronutrients,
      ingredients = ingredients,
      cooking = cooking.map(RecipeInput.CookingItem::confirm),
    )

    is RecipeProcessedInput.Encrypted -> EncryptedRecipe(
      info = EncryptedRecipeInfo(
        meta = meta,
        isOwned = isOwned,
        isSaved = isSaved,
        categories = categories,
        isFavourite = isFavourite,
        servings = servings,
        time = time,
        calories = calories,
        name = name,
        preview = (pictures.preview as? RecipeInput.Picture.Uploaded)?.path,
      ),
      description = description,
      macronutrients = macronutrients,
      ingredients = ingredients,
      cooking = cooking,
      cookingPictures = pictures.cooking
        .mapValues { step ->
          step.value.mapNotNull { picture ->
            (picture as? RecipeInput.Picture.Uploaded)?.path
          }
        }
    )
  }
}

internal fun EncryptedRecipe.decryptByInput(input: RecipeInput) = DecryptedRecipe(
  info = DecryptedRecipeInfo(
    meta = info.meta,
    isOwned = info.isOwned,
    isSaved = info.isSaved,
    categories = info.categories,
    isFavourite = info.isFavourite,
    servings = info.servings,
    time = info.time,
    calories = info.calories,
    name = input.name,
    preview = info.preview,
  ),
  macronutrients = input.macronutrients,
  description = input.description,
  ingredients = input.ingredients,
  cooking = input.cooking.map { it.confirm() },
)

internal fun RecipeInput.CookingItem.confirm() =
  when (this) {
    is RecipeInput.CookingItem.Step -> Recipe.Decrypted.CookingItem.Step(
      id = id,
      description = description,
      time = time,
      pictures = pictures.filterIsInstance<RecipeInput.Picture.Uploaded>()
        .map(RecipeInput.Picture.Uploaded::path),
      recipeId = recipeId,
    )

    is RecipeInput.CookingItem.Section -> Recipe.Decrypted.CookingItem.Section(
      id = id,
      name = name,
    )
  }

internal fun RecipeInput.asDecrypted() = DecryptedRecipeInput(
  id = id,

  name = name.trim(),
  visibility = visibility,
  isEncrypted = hasEncryption,
  language = language,
  description = description?.trim(),
  preview = preview,

  servings = servings,
  time = time,

  calories = calories,
  macronutrients = macronutrients,

  ingredients = ingredients,
  cooking = cooking,

  version = version,
)