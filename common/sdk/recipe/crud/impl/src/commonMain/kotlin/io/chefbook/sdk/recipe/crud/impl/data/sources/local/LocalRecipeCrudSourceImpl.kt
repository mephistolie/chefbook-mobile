package io.chefbook.sdk.recipe.crud.impl.data.sources.local

import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.utils.result.EmptyResult
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
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalRecipeCrudSource {

  private val queries = database.recipeQueries
  private val recipeCategoryQueries = database.recipeCategoryQueries

  override suspend fun createRecipe(recipe: Recipe) = safeQueryResult {
    queries.insert(recipe.toDto())
    recipe.id
  }

  override suspend fun getRecipe(recipeId: String) = safeQueryResult {
    val recipeCategories = recipeCategoryQueries.selectAll().executeAsList()
    queries.select(recipeId).executeAsOne().toEntity(recipeCategories)
  }

  override suspend fun updateRecipe(recipe: Recipe) = safeQueryResult {
    val dto = recipe.toDto()
    queries.update(
      recipe_id = dto.recipe_id, name = dto.name,
      owner_id = dto.owner_id, owner_name = dto.owner_name, owner_avatar = dto.owner_avatar,
      owned = dto.owned, saved = dto.saved, visibility = dto.visibility, encrypted = dto.encrypted,
      language = dto.language, description = dto.description,
      creation_timestamp = dto.creation_timestamp, update_timestamp = dto.update_timestamp, version = dto.version,
      rating = dto.rating, score = dto.score, votes = dto.votes,
      tags = dto.tags, favourite = dto.favourite,
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
      recipe_id = recipeId,
      owner_name = name,
      owner_avatar = avatar,
    )
  }

  override suspend fun setRecipeTags(recipeId: String, tags: List<Tag>) = safeQueryResult {
    queries.setTags(
      recipe_id = recipeId,
      tags = Json.encodeToString(tags.toSerializable()),
    )
  }

  override suspend fun deleteRecipe(recipeId: String) = safeQueryResult {
    queries.delete(recipeId)
  }
}
