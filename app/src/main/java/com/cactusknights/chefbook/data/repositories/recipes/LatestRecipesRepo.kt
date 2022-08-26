package com.cactusknights.chefbook.data.repositories.recipes

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.LatestRecipesProto
import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

@Singleton
class LatestRecipesRepo @Inject constructor(
    private val dataStore: DataStore<LatestRecipesProto>,
): ILatestRecipesRepo {

    private val saved get() = dataStore.data.take(1)

    override suspend fun observeLatestRecipes(): Flow<List<Int>> = dataStore.data.map { it.latestRecipesList }

    override suspend fun getLatestRecipes(): List<Int> = saved.first().latestRecipesList

    override suspend fun pushRecipe(recipeId: Int) {
        val savedLatestRecipes = saved.first().latestRecipesList.filter { it != recipeId }
        var latestRecipes = listOf(recipeId) + savedLatestRecipes
        if (latestRecipes.size > LATEST_RECIPES_CACHE_SIZE) {
            latestRecipes = latestRecipes.subList(0, LATEST_RECIPES_CACHE_SIZE)
        }
        dataStore.updateData {
            it.toBuilder().clearLatestRecipes().addAllLatestRecipes(latestRecipes). build()
        }
    }

    companion object {
        private const val LATEST_RECIPES_CACHE_SIZE = 10
    }

}
