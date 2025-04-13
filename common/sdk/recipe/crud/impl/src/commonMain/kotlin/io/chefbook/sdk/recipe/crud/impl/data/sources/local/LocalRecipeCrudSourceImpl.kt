package io.chefbook.sdk.recipe.crud.impl.data.sources.local

import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.internal.data.sources.local.sql.dto.toDto
import io.chefbook.sdk.recipe.core.api.internal.data.sources.local.sql.dto.toEntity
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.local.LocalRecipeCrudSource
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.toSerializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class LocalRecipeCrudSourceImpl(
  private val profileId: String,
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalRecipeCrudSource {

  private val queries = database.recipeQueries

  override suspend fun createRecipe(recipe: Recipe) = safeQueryResult {
    queries.transaction {
      queries.insert(recipe.toDto())
      queries.addToRecipeBook(recipe.id, profileId)
    }
    recipe.id
  }

  override suspend fun getRecipe(recipeId: String) = safeQueryResult {
    queries.transactionWithResult {
      val collections = queries.getCollections(recipeId, profileId).executeAsList()
      queries.select(recipeId, profileId).executeAsOne().toEntity(collections)
    }
  }

  override suspend fun updateRecipe(recipe: Recipe) = safeQueryResult {
    val dto = recipe.toDto()
    queries.update(
      recipeId = dto.recipeId, name = dto.name,
      ownerId = dto.ownerId, ownerName = dto.ownerName, ownerAvatar = dto.ownerAvatar,
      visibility = dto.visibility, encrypted = dto.encrypted,
      language = dto.language, description = dto.description,
      creationTimestamp = dto.creationTimestamp, updateTimestamp = dto.updateTimestamp, version = dto.version,
      rating = dto.rating, score = dto.score, votes = dto.votes,
      tags = dto.tags,
      servings = dto.servings, time = dto.time,
      calories = dto.calories, protein = dto.protein, fats = dto.fats, carbohydrates = dto.carbohydrates,
      ingredients = dto.ingredients, cooking = dto.cooking, pictures = dto.pictures,
    )
  }

  override suspend fun setRecipeOwnerInfo(
    recipeId: String,
    name: String?,
    avatar: String?,
  ) = safeQueryResult {
    queries.setOwnerInfo(
      recipeId = recipeId,
      ownerName = name,
      ownerAvatar = avatar,
    )
  }

  override suspend fun setRecipeTags(recipeId: String, tags: List<Tag>) = safeQueryResult {
    queries.setTags(
      recipeId = recipeId,
      tags = Json.encodeToString(tags.toSerializable()),
    )
  }

  override suspend fun deleteRecipe(recipeId: String) = safeQueryResult {
    queries.delete(recipeId)
  }
}
