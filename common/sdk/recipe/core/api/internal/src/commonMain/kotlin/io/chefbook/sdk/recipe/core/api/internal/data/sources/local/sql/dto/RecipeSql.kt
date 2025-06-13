package io.chefbook.sdk.recipe.core.api.internal.data.sources.local.sql.dto

import io.chefbook.libs.models.language.LanguageMapper
import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.models.visibility.Visibility
import io.chefbook.sdk.database.api.internal.GetCollections
import io.chefbook.sdk.database.api.`internal`.recipe.Select
import io.chefbook.sdk.database.api.internal.recipe.SelectAll
import io.chefbook.sdk.database.api.internal.toBoolean
import io.chefbook.sdk.database.api.internal.toLong
import io.chefbook.sdk.recipe.core.api.external.domain.entities.CollectionInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.IngredientItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.PicturesSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.toSerializable
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.toSerializable
import kotlinx.serialization.json.Json
import io.chefbook.sdk.database.api.internal.Recipes as RecipeSql

fun SelectAll.toEntity(
  collections: List<GetCollections>
): RecipeInfo {
  val pictures: PicturesSerializable = Json.decodeFromString(pictures)

  val meta = RecipeMeta(
    id = recipeId,
    owner = ProfileInfo(
      id = ownerId,
      name = ownerName,
      avatar = ownerAvatar,
    ),

    visibility = Visibility.deserialize(visibility),
    isEncryptionEnabled = encrypted.toBoolean(),

    language = LanguageMapper.map(language),

    version = version.toInt(),
    creationTimestamp = creationTimestamp,
    updateTimestamp = updateTimestamp,

    rating = RecipeMeta.Rating(
      index = rating.toFloat(),
      score = score?.toInt(),
      votes = votes.toInt(),
    ),

    tags = Json.decodeFromString<TagsSerializable>(tags).toEntity(),
  )

  return if (meta.isEncryptionEnabled) {
    EncryptedRecipeInfo(
      meta = meta,
      isOwned = owned,
      isSaved = true,
      collections = collections.toEntities(),
      isFavourite = favourite.toBoolean(),
      servings = servings?.toInt(),
      time = time?.toInt(),
      calories = calories?.toInt(),
      name = name,
      preview = pictures.preview,
    )
  } else {
    DecryptedRecipeInfo(
      meta = meta,
      isOwned = owned,
      isSaved = true,
      collections = collections.toEntities(),
      isFavourite = favourite.toBoolean(),
      servings = servings?.toInt(),
      time = time?.toInt(),
      calories = calories?.toInt(),
      name = name,
      preview = pictures.preview,
    )
  }
}

fun Select.toEntity(
  collections: List<GetCollections>
): Recipe {
  var macronutrients: Recipe.Macronutrients? = null
  if (protein != null || fats != null || carbohydrates != null) {
    macronutrients = Recipe.Macronutrients(protein?.toInt(), fats?.toInt(), carbohydrates?.toInt())
  }

  val pictures: PicturesSerializable = Json.decodeFromString(pictures)

  val meta = RecipeMeta(
    id = recipeId,
    owner = ProfileInfo(
      id = ownerId,
      name = ownerName,
      avatar = ownerAvatar,
    ),

    visibility = Visibility.deserialize(visibility),
    isEncryptionEnabled = encrypted.toBoolean(),

    language = LanguageMapper.map(language),

    version = version.toInt(),
    creationTimestamp = creationTimestamp,
    updateTimestamp = updateTimestamp,

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
        isOwned = owned,
        isSaved = true,
        collections = collections.toEntities(),
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
        isOwned = owned,
        isSaved = true,
        collections = collections.toEntities(),
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
      recipeId = id,
      name = name,

      ownerId = owner.id,
      ownerName = owner.name,
      ownerAvatar = owner.avatar,

      visibility = visibility.serialize(),
      encrypted = isEncryptionEnabled.toLong(),

      language = language.code,
      description = description,

      creationTimestamp = creationTimestamp.toString(),
      updateTimestamp = updateTimestamp.toString(),
      version = version.toLong(),

      rating = rating.index.toDouble(),
      votes = rating.votes.toLong(),

      tags = Json.encodeToString(tags.toSerializable()),

      servings = servings?.toLong(),
      time = time?.toLong(),

      calories = calories?.toLong(),
      protein = macronutrients?.protein?.toLong(),
      fats = macronutrients?.fats?.toLong(),
      carbohydrates = macronutrients?.carbohydrates?.toLong(),

      ingredients = Json.encodeToString(ingredients.map(Recipe.Decrypted.IngredientsItem::toSerializable)),
      cooking = Json.encodeToString(cooking.map(Recipe.Decrypted.CookingItem::toSerializable)),
      pictures = Json.encodeToString(
        cooking
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
      recipeId = id,
      name = name,

      ownerId = owner.id,
      ownerName = owner.name,
      ownerAvatar = owner.avatar,

      visibility = visibility.serialize(),
      encrypted = isEncryptionEnabled.toLong(),

      language = language.code,
      description = description,

      creationTimestamp = creationTimestamp.toString(),
      updateTimestamp = updateTimestamp.toString(),
      version = version.toLong(),

      rating = rating.index.toDouble(),
      votes = rating.votes.toLong(),

      tags = Json.encodeToString(tags.toSerializable()),

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

fun List<GetCollections>.toEntities() =
  mapNotNull { collection ->
    val id = collection.collectionId
    val name = collection.name

    if (id == null || name == null) return@mapNotNull null
    CollectionInfo(
      id = id,
      name = name,
    )
  }
