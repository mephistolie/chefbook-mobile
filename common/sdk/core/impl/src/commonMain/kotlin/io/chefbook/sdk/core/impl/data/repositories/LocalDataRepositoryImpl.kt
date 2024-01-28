package io.chefbook.sdk.core.impl.data.repositories

import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.RecipeBookRepository
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

class LocalDataRepositoryImpl(
  private val profileRepository: ProfileRepository,
  private val recipeBookRepository: RecipeBookRepository,
  private val latestRecipesRepository: LatestRecipesRepository,
  private val recipeEncryptionRepository: RecipeEncryptionRepository,
  private val categoryRepository: CategoryRepository,
  private val shoppingListRepository: ShoppingListRepository,
) : LocalDataRepository {

  override suspend fun refreshData() {
    profileRepository.refreshProfile()
    recipeBookRepository.refreshRecipeBook()
    shoppingListRepository.refreshShoppingList()
  }

  override suspend fun clearLocalData() {
    profileRepository.clearLocalData()
    recipeBookRepository.clearLocalData(exceptProfileId = Profile.LOCAL_PROFILE_ID)
    latestRecipesRepository.clear()
    recipeEncryptionRepository.clearLocalData()
    categoryRepository.clearLocalData(exceptProfileId = Profile.LOCAL_PROFILE_ID)
    shoppingListRepository.clearLocalData()
  }
}
