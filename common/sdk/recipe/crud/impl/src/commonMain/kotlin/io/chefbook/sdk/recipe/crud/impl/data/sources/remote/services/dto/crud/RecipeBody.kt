package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.libs.models.language.LanguageMapper
import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.VisibilitySerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.IngredientItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.ProfileBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RatingBody
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.PicturesBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RecipeBody(
  @SerialName("id")
  val id: String,
  @SerialName("name")
  val name: String,

  @SerialName("owner")
  val owner: ProfileBody,

  @SerialName("owned")
  val isOwned: Boolean = false,
  @SerialName("saved")
  val isSaved: Boolean = false,
  @SerialName("visibility")
  val visibility: VisibilitySerializable,
  @SerialName("encrypted")
  val isEncrypted: Boolean,

  @SerialName("language")
  val language: String,
  @SerialName("description")
  val description: String? = null,

  @SerialName("creationTimestamp")
  val creationTimestamp: String,
  @SerialName("updateTimestamp")
  val updateTimestamp: String,
  @SerialName("version")
  val version: Int,

  @SerialName("rating")
  val rating: RatingBody? = null,

  @SerialName("tags")
  val tags: List<String>? = null,
  @SerialName("categories")
  val categories: List<String>? = null,
  @SerialName("favourite")
  val isFavourite: Boolean = false,

  @SerialName("servings")
  val servings: Int? = null,
  @SerialName("time")
  val time: Int? = null,

  @SerialName("calories")
  val calories: Int? = null,
  @SerialName("macronutrients")
  val macronutrients: MacronutrientsBody? = null,

  @SerialName("ingredients")
  val ingredients: List<IngredientItemSerializable>,
  @SerialName("cooking")
  val cooking: List<CookingItemSerializable>,
  @SerialName("pictures")
  val pictures: PicturesBody? = null,
)

internal fun RecipeBody.toEntity(categoriesMap: Map<String, Category>): Recipe {
  val meta = RecipeMeta(
    id = id,
    owner = ProfileInfo(
      id = owner.id,
      name = owner.name,
      avatar = owner.avatar,
    ),
    visibility = when (visibility) {
      VisibilitySerializable.PUBLIC -> RecipeMeta.Visibility.PUBLIC
      VisibilitySerializable.LINK -> RecipeMeta.Visibility.LINK
      else -> RecipeMeta.Visibility.PRIVATE
    },
    isEncryptionEnabled = isEncrypted,
    language = LanguageMapper.map(language),
    version = version,
    creationTimestamp = creationTimestamp,
    updateTimestamp = updateTimestamp,
    rating = RecipeMeta.Rating(
      index = rating?.index ?: 0F,
      score = rating?.score,
      votes = rating?.votes ?: 0,
    )
  )

  return if (ingredients.getOrNull(0)?.type == IngredientItemSerializable.TYPE_ENCRYPTED_DATA) {
    EncryptedRecipe(
      info = EncryptedRecipeInfo(
        meta = meta,
        name = name,
        isOwned = isOwned,
        isSaved = isSaved,
        preview = pictures?.preview,
        categories = categories?.mapNotNull(categoriesMap::get).orEmpty(),
        isFavourite = isFavourite,
        servings = servings,
        time = time,
        calories = calories,
      ),
      macronutrients = macronutrients?.toEntity(),
      description = description,
      ingredients = ingredients.getOrNull(0)?.text.orEmpty(),
      cooking = cooking.getOrNull(0)?.text.orEmpty(),
      cookingPictures = pictures?.cooking.orEmpty(),
    )
  } else {
    DecryptedRecipe(
      info = DecryptedRecipeInfo(
        meta = meta,
        name = name,
        isOwned = isOwned,
        isSaved = isSaved,
        preview = pictures?.preview,
        categories = categories?.mapNotNull(categoriesMap::get).orEmpty(),
        isFavourite = isFavourite,
        servings = servings,
        time = time,
        calories = calories,
      ),
      macronutrients = macronutrients?.toEntity(),
      description = description,
      ingredients = ingredients.map(IngredientItemSerializable::toEntity),
      cooking = cooking.map { step -> step.toEntity(pictures?.cooking?.get(step.id)) }
    )
  }
}