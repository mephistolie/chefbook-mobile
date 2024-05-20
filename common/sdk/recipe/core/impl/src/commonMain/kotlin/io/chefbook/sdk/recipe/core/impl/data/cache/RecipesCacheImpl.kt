package io.chefbook.sdk.recipe.core.impl.data.cache

import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.category.api.internal.data.cache.CategoriesCacheReader
import io.chefbook.sdk.recipe.book.api.external.domain.entities.RecipeBook
import io.chefbook.sdk.recipe.book.api.internal.data.cache.RecipeBookCache
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update

internal class RecipesCacheImpl(
  private val categoriesCache: CategoriesCacheReader,
  scopes: CoroutineScopes,
) : RecipeBookCache {

  private val cachedRecipeInfo = MutableStateFlow<List<RecipeInfo>?>(null)
  private val cachedRecipeMap = MutableStateFlow<Map<String, Recipe>>(emptyMap())

  @OptIn(ExperimentalCoroutinesApi::class)
  private val cachedRecipeBook = combine(
    cachedRecipeInfo
      .asStateFlow()
      .mapLatest { recipes -> recipes?.filter(RecipeInfo::isSaved) },
    categoriesCache.observeCategories(),
  ) { recipes, categories ->
    if (recipes != null || categories != null) {
      RecipeBook(
        recipes = recipes.orEmpty(),
        categories = categories.orEmpty(),
      )
    } else {
      null
    }
  }
    .shareIn(scopes.repository, started = SharingStarted.Lazily, replay = 1)

  override fun observeRecipes() =
    cachedRecipeMap

  override fun observeRecipeBook(): Flow<RecipeBook?> =
    cachedRecipeBook

  override suspend fun getRecipeBook(): RecipeBook =
    RecipeBook(
      recipes = cachedRecipeInfo.value.orEmpty()
        .filter(RecipeInfo::isSaved),
      categories = categoriesCache.getCategories(),
    )

  override fun observeRecipe(recipeId: String): Flow<Recipe?> = cachedRecipeMap
    .map { it[recipeId] }

  override suspend fun getRecipe(recipeId: String): Recipe? {
    val recipe = cachedRecipeMap.value[recipeId]
    Logger.i("Requested recipe $recipeId is found: ${recipe != null}")
    return recipe
  }

  override suspend fun setRecipeBook(recipes: List<RecipeInfo>) {
    cachedRecipeInfo.emit(recipes)
    removeOutdatedRecipes(recipes)
    Logger.i("${recipes.size} recipes were cached")
  }

  private fun removeOutdatedRecipes(newRecipeBook: List<RecipeInfo>) = cachedRecipeMap.update { recipesCache ->
    val recipeBookIds = newRecipeBook.map { it.id }
    newRecipeBook
      .mapNotNull { newRecipeInfo ->
        val cachedRecipe = recipesCache[newRecipeInfo.id]
        if (cachedRecipe != null && cachedRecipe.version >= newRecipeInfo.version) cachedRecipe else null
      }
      .plus(recipesCache.values.filter { it.id !in recipeBookIds })
      .associateBy { it.id }
  }

  override suspend fun putRecipe(recipe: Recipe) {
    transformRecipeCache(recipe.id) { recipe }
    cachedRecipeInfo.update { recipes -> recipes?.filter { it.id != recipe.id }?.plus(recipe.info) }
    Logger.i("Recipe ${recipe.id} was put")
  }

  override suspend fun removeRecipe(recipeId: String) {
    transformRecipeCache(recipeId) { null }
    cachedRecipeInfo.update { recipeBook -> recipeBook?.filter { it.id != recipeId } }
    Logger.i("Recipe $recipeId removed")
  }

  override suspend fun setRecipeScore(recipeId: String, score: Int?) {
    transformRecipeCache(recipeId) { it?.withScore(score) }
    transformRecipeBookCache(recipeId) { it.withScore(score) }
    Logger.i("Recipe $recipeId score changed to $score")
  }

  override suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean) {
    transformRecipeCache(recipeId) { it?.withSavedStatus(saved) }
    transformRecipeBookCache(recipeId) { it.withSavedStatus(saved) }
    Logger.i("Recipe $recipeId saved status changed to $saved")
  }

  override suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean) {
    transformRecipeCache(recipeId) { it?.withFavouriteStatus(isFavourite = favourite) }
    transformRecipeBookCache(recipeId) { it.withFavouriteStatus(isFavourite = favourite) }
    Logger.i("Recipe $recipeId favourite changed to $favourite")
  }

  override suspend fun setRecipeCategories(recipeId: String, categories: List<String>) {
    val recipeCategories = categoriesCache.getCategories().filter { it.id in categories }
    transformRecipeCache(recipeId) { it?.withCategories(categories = recipeCategories) }
    transformRecipeBookCache(recipeId) { it.withCategories(categories = recipeCategories) }
    Logger.i("Recipe $recipeId categories $categories were set")
  }

  private fun transformRecipeBookCache(
    recipeId: String,
    transform: (RecipeInfo) -> RecipeInfo
  ) = cachedRecipeInfo.update { recipeBook ->
    recipeBook?.map { if (it.id != recipeId) it else transform(it) }
  }

  private fun transformRecipeCache(
    recipeId: String,
    transform: (Recipe?) -> Recipe?
  ) = cachedRecipeMap.update { recipeMap ->
    recipeMap.toMutableMap().apply {
      val recipe = transform(get(recipeId))
      if (recipe != null) put(recipeId, recipe) else remove(recipeId)
    }
  }

  override suspend fun clear() {
    cachedRecipeInfo.emit(null)
    cachedRecipeMap.emit(emptyMap())
  }
}
