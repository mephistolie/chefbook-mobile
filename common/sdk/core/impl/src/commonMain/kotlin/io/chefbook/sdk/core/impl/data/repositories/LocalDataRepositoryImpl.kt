package io.chefbook.sdk.core.impl.data.repositories

import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.RecipeBookRepository
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

class LocalDataRepositoryImpl(
//  private val profileRepository: ProfileRepository,
//  private val recipeBookRepository: RecipeBookRepository,
//  private val latestRecipesRepository: LatestRecipesRepository,
//  private val recipeEncryptionRepository: RecipeEncryptionRepository,
//  private val collectionRepository: CollectionRepository,
//  private val shoppingListRepository: ShoppingListRepository,
) : LocalDataRepository {

  override suspend fun refreshData() {
//    profileRepository.refreshProfile()
//    recipeBookRepository.refreshRecipeBook()
//    shoppingListRepository.refreshShoppingList()
  }

  override suspend fun clearLocalData(profileId: String) {
//    profileRepository.clearLocalData(profileId)
//    recipeBookRepository.clearUnusedRecipes()
//    latestRecipesRepository.clear()
//    recipeEncryptionRepository.clearLocalData()
//    collectionRepository.clearUnusedCollections()
//    shoppingListRepository.clearLocalData()
  }
}
