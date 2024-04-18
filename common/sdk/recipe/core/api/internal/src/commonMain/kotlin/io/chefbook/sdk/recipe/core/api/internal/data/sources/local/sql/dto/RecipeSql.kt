package io.chefbook.sdk.recipe.core.api.internal.data.sources.local.sql.dto

import io.chefbook.libs.models.language.LanguageMapper
import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.database.api.internal.toBoolean
import io.chefbook.sdk.database.api.internal.toLong
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.IngredientItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.PicturesSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.toSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.VisibilitySerializable
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.toSerializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import io.chefbook.sdk.database.api.internal.Recipe as RecipeSql
import io.chefbook.sdk.database.api.internal.SelectAll as RecipeCategory

fun RecipeSql.toEntity(
  categories: List<RecipeCategory>
): Recipe {
  var macronutrients: Recipe.Macronutrients? = null
  if (protein != null || fats != null || carbohydrates != null) {
    macronutrients = Recipe.Macronutrients(protein?.toInt(), fats?.toInt(), carbohydrates?.toInt())
  }

  val pictures: PicturesSerializable = Json.decodeFromString(pictures)

  val meta = RecipeMeta(
    id = recipe_id,
    owner = ProfileInfo(
      id = owner_id,
      name = owner_name,
      avatar = owner_avatar,
    ),

    visibility = when (visibility.lowercase()) {
      VisibilitySerializable.PUBLIC.name.lowercase() -> RecipeMeta.Visibility.PUBLIC
      VisibilitySerializable.LINK.name.lowercase() -> RecipeMeta.Visibility.LINK
      else -> RecipeMeta.Visibility.PRIVATE
    },
    isEncryptionEnabled = encrypted.toBoolean(),

    language = LanguageMapper.map(language),

    version = version.toInt(),
    creationTimestamp = creation_timestamp,
    updateTimestamp = update_timestamp,

    rating = RecipeMeta.Rating(
      index = rating.toFloat(),
      score = score?.toInt(),
      votes = votes.toInt(),
    ),

    tags = Json.decodeFromString<TagsSerializable>(tags).toEntity(),
  )

  return if (meta.isEncryptionEnabled) {
    EncryptedRecipe(
      info = EncryptedRecipeInfo(
        meta = meta,
        isOwned = owned.toBoolean(),
        isSaved = saved.toBoolean(),
        categories = categories.toEntities(recipe_id),
        isFavourite = favourite.toBoolean(),
        servings = servings?.toInt(),
        time = time?.toInt(),
        calories = calories?.toInt(),
        name = name,
        preview = pictures.preview,
      ),
      description = description,
      macronutrients = macronutrients,
      ingredients = ingredients,
      cooking = cooking,
      cookingPictures = pictures.cooking,
    )
  } else {
    val ingredients: List<IngredientItemSerializable> = Json.decodeFromString(ingredients)
    val cooking: List<CookingItemSerializable> = Json.decodeFromString(cooking)

    DecryptedRecipe(
      info = DecryptedRecipeInfo(
        meta = meta,
        isOwned = owned.toBoolean(),
        isSaved = saved.toBoolean(),
        categories = categories.toEntities(recipe_id),
        isFavourite = favourite.toBoolean(),
        servings = servings?.toInt(),
        time = time?.toInt(),
        calories = calories?.toInt(),
        name = name,
        preview = pictures.preview,
      ),
      description = description,
      macronutrients = macronutrients,
      ingredients = ingredients.map(IngredientItemSerializable::toEntity),
      cooking = cooking.map { it.toEntity(pictures.cooking[it.id]) },
    )
  }
}

fun Recipe.toDto() =
  when (this) {
    is Recipe.Decrypted -> RecipeSql(
      recipe_id = id,
      name = name,

      owner_id = owner.id,
      owner_name = owner.name,
      owner_avatar = owner.avatar,

      owned = isOwned.toLong(),
      saved = isSaved.toLong(),
      visibility = when (visibility) {
        RecipeMeta.Visibility.PRIVATE -> VisibilitySerializable.PRIVATE.name.lowercase()
        RecipeMeta.Visibility.LINK -> VisibilitySerializable.LINK.name.lowercase()
        RecipeMeta.Visibility.PUBLIC -> VisibilitySerializable.PUBLIC.name.lowercase()
      },
      encrypted = isEncryptionEnabled.toLong(),

      language = language.code,
      description = description,

      creation_timestamp = creationTimestamp.toString(),
      update_timestamp = updateTimestamp.toString(),
      version = version.toLong(),

      rating = rating.index.toDouble(),
      score = rating.score?.toLong(),
      votes = rating.votes.toLong(),

      tags = Json.encodeToString(tags.toSerializable()),
      favourite = isFavourite.toLong(),

      servings = servings?.toLong(),
      time = time?.toLong(),

      calories = calories?.toLong(),
      protein = macronutrients?.protein?.toLong(),
      fats = macronutrients?.fats?.toLong(),
      carbohydrates = macronutrients?.carbohydrates?.toLong(),

      ingredients = Json.encodeToString(ingredients.map(Recipe.Decrypted.IngredientsItem::toSerializable)),
      cooking = Json.encodeToString(cooking.map(Recipe.Decrypted.CookingItem::toSerializable)),
      pictures = Json.encodeToString(cooking
        .filterIsInstance<Recipe.Decrypted.CookingItem.Step>()
        .associate { it.id to it.pictures }
        .let { cooking ->
          PicturesSerializable(
            preview = preview,
            cooking = cooking,
          )
        }
      ),
    )

    is Recipe.Encrypted -> RecipeSql(
      recipe_id = id,
      name = name,

      owner_id = owner.id,
      owner_name = owner.name,
      owner_avatar = owner.avatar,

      owned = isOwned.toLong(),
      saved = isSaved.toLong(),
      visibility = when (visibility) {
        RecipeMeta.Visibility.PRIVATE -> VisibilitySerializable.PRIVATE.name.lowercase()
        RecipeMeta.Visibility.LINK -> VisibilitySerializable.LINK.name.lowercase()
        RecipeMeta.Visibility.PUBLIC -> VisibilitySerializable.PUBLIC.name.lowercase()
      },
      encrypted = isEncryptionEnabled.toLong(),

      language = language.code,
      description = description,

      creation_timestamp = creationTimestamp.toString(),
      update_timestamp = updateTimestamp.toString(),
      version = version.toLong(),

      rating = rating.index.toDouble(),
      score = rating.score?.toLong(),
      votes = rating.votes.toLong(),

      tags = Json.encodeToString(tags.toSerializable()),
      favourite = isFavourite.toLong(),

      servings = servings?.toLong(),
      time = time?.toLong(),

      calories = calories?.toLong(),
      protein = macronutrients?.protein?.toLong(),
      fats = macronutrients?.fats?.toLong(),
      carbohydrates = macronutrients?.carbohydrates?.toLong(),

      ingredients = ingredients,
      cooking = cooking,
      pictures = Json.encodeToString(
        PicturesSerializable(
          preview = preview,
          cooking = cookingPictures,
        )
      ),
    )
  }

fun List<RecipeCategory>.toEntities(recipeId: String) =
  mapNotNull {
    if (it.recipe_id != recipeId) {
      null
    } else {
      Category(
        id = it.category_id,
        name = it.name.orEmpty(),
        emoji = it.emoji,
      )
    }
  }