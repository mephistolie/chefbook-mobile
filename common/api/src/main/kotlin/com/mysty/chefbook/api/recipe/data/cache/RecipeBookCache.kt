package com.mysty.chefbook.api.recipe.data.cache

import com.mysty.chefbook.api.category.data.cache.ICategoriesCacheReader
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.toRecipeInfo
import com.mysty.chefbook.core.coroutines.AppDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import timber.log.Timber

internal interface IRecipeBookCacheReader {
  fun observeRecipeBook(): Flow<List<RecipeInfo>?>
  suspend fun getRecipeBook(): List<RecipeInfo>
  fun observeRecipe(recipeId: String): Flow<Recipe?>
  suspend fun getRecipe(recipeId: String): Recipe?
}

internal interface IRecipeBookCacheWriter {
  suspend fun setRecipeBook(recipes: List<RecipeInfo>)
  suspend fun putRecipe(recipe: Recipe)
  suspend fun removeRecipe(recipeId: String)

  suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean)
  suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean)
  suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean)
  suspend fun setRecipeCategories(recipeId: String, categories: List<String>)

}

internal interface IRecipeBookCache : IRecipeBookCacheReader, IRecipeBookCacheWriter

internal class RecipeBookCache(
  private val categoriesCache: ICategoriesCacheReader,
  dispatchers: AppDispatchers,
) : IRecipeBookCache {

  @OptIn(ExperimentalCoroutinesApi::class)
  private val dispatcher = dispatchers.default.limitedParallelism(1)

  private val cachedRecipeBook = MutableStateFlow<List<RecipeInfo>?>(null)
  private val cachedRecipeMap = MutableStateFlow<Map<String, Recipe>>(emptyMap())

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun observeRecipeBook(): Flow<List<RecipeInfo>?> = cachedRecipeBook
    .asStateFlow()
    .mapLatest { recipes -> recipes?.filter { it.isSaved } }

  override suspend fun getRecipeBook(): List<RecipeInfo> = cachedRecipeBook.value
    .orEmpty()
    .filter { it.isSaved }

  override fun observeRecipe(recipeId: String): Flow<Recipe?> = cachedRecipeMap
    .map { it[recipeId] }

  override suspend fun getRecipe(recipeId: String): Recipe? {
    val recipe = cachedRecipeMap.value[recipeId]
    Timber.i("Requested recipe $recipeId is found: ${recipe != null}")
    return recipe
  }

  override suspend fun setRecipeBook(recipes: List<RecipeInfo>) {
    cachedRecipeBook.emit(recipes)
    removeOutdatedRecipes(recipes)
    Timber.i("${recipes.size} recipes were cached")
  }

  private suspend fun removeOutdatedRecipes(newRecipeBook: List<RecipeInfo>) = with(dispatcher) {
    val recipeBookIds = newRecipeBook.map { it.id }
    val recipesCache = cachedRecipeMap.value
    val relevantRecipes = mutableListOf<Recipe>()

    newRecipeBook.forEach { newRecipeInfo ->
      val cachedRecipe = recipesCache[newRecipeInfo.id]
      if (cachedRecipe != null && cachedRecipe.updateTimestamp >= newRecipeInfo.updateTimestamp) {
        relevantRecipes.add(cachedRecipe)
      }
    }
    relevantRecipes.addAll(recipesCache.values.filter { it.id !in recipeBookIds })

    cachedRecipeMap.emit(relevantRecipes.associateBy { it.id })
  }

  override suspend fun putRecipe(recipe: Recipe) = withContext(dispatcher) {
    transformRecipeCache(recipe.id) { recipe }
    val updatedRecipes = cachedRecipeBook.value?.filter { it.id != recipe.id }?.toMutableList()
    updatedRecipes?.add(recipe.toRecipeInfo())
    cachedRecipeBook.emit(updatedRecipes)
    Timber.i("Recipe ${recipe.id} was put")
  }

  override suspend fun removeRecipe(recipeId: String) = withContext(dispatcher) {
    transformRecipeCache(recipeId) { null }
    cachedRecipeBook.emit(cachedRecipeBook.value?.filter { it.id != recipeId })
    Timber.i("Recipe $recipeId removed")
  }

  override suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean) {
    transformRecipeCache(recipeId) {
      it?.copy(
        isSaved = saved,
        isFavourite = if (saved) it.isFavourite else false,
        categories = if (saved) it.categories else emptyList(),
      )
    }
    transformRecipeBookCache(recipeId) {
      it.copy(
        isSaved = saved,
        isFavourite = if (saved) it.isFavourite else false,
        categories = if (saved) it.categories else emptyList(),
      )
    }
    Timber.i("Recipe $recipeId saved status changed to $saved")
  }

  override suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean) {
    transformRecipeCache(recipeId) { updateRecipeLikes(it, liked) }
    transformRecipeBookCache(recipeId) { updateRecipeLikes(it, liked) }
    Timber.i("Recipe $recipeId like status changed to $liked")
  }

  private fun updateRecipeLikes(recipe: RecipeInfo, liked: Boolean) = recipe.copy(
    isLiked = liked,
    likes = if (liked != recipe.isLiked && recipe.likes != null) {
      if (liked) maxOf(recipe.likes + 1, 1) else maxOf(recipe.likes - 1, 0)
    } else recipe.likes
  )

  private fun updateRecipeLikes(recipe: Recipe?, liked: Boolean) = recipe?.copy(
    isLiked = liked,
    likes = if (liked != recipe.isLiked && recipe.likes != null) {
      if (liked) maxOf(recipe.likes + 1, 1) else maxOf(recipe.likes - 1, 0)
    } else recipe.likes
  )

  override suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean) {
    transformRecipeCache(recipeId) { it?.copy(isFavourite = favourite) }
    transformRecipeBookCache(recipeId) { it.copy(isFavourite = favourite) }
    Timber.i("Recipe $recipeId favourite changed to $favourite")
  }

  override suspend fun setRecipeCategories(recipeId: String, categories: List<String>) {
    val recipeCategories = categoriesCache.getCategories().filter { it.id in categories }
    transformRecipeCache(recipeId) { it?.copy(categories = recipeCategories) }
    transformRecipeBookCache(recipeId) { it.copy(categories = recipeCategories) }
    Timber.i("Recipe $recipeId categories $categories were set")
  }

  private suspend fun transformRecipeBookCache(
    recipeId: String,
    transform: (RecipeInfo) -> RecipeInfo
  ) = withContext(dispatcher) {
    cachedRecipeBook.emit(cachedRecipeBook.value?.map { if (it.id != recipeId) it else transform(it) })
  }

  private suspend fun transformRecipeCache(
    recipeId: String,
    transform: (Recipe?) -> Recipe?
  ) = withContext(dispatcher) {
    val recipeMap = cachedRecipeMap.value.toMutableMap()
    recipeMap[recipeId]
      .let(transform)
      .let { if (it != null) recipeMap.put(recipeId, it) else recipeMap.remove(recipeId) }
    cachedRecipeMap.value = recipeMap.toMap()
  }
}
