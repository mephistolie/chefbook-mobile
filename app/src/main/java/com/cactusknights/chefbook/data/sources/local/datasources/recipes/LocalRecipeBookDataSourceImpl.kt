package com.cactusknights.chefbook.data.sources.local.datasources.recipes

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.SyncDataProto
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo
import com.cactusknights.chefbook.data.LocalRecipeBookDataSource
import com.cactusknights.chefbook.data.sources.local.dao.RecipeBookDao
import com.cactusknights.chefbook.data.sources.local.entities.toRecipe
import com.cactusknights.chefbook.data.sources.local.entities.toRecipeEntity
import com.cactusknights.chefbook.data.sources.local.entities.info
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class LocalRecipeBookDataSourceImpl @Inject constructor(
    private val recipeBookDao: RecipeBookDao,
    private val syncData: DataStore<SyncDataProto>,
) : LocalRecipeBookDataSource {

    override suspend fun getRecipeBook(): ArrayList<RecipeInfo> {
        val recipesInfo = arrayListOf<RecipeInfo>()
        recipeBookDao.getRecipes().map { it.info() }.toCollection(recipesInfo)
        recipesInfo.forEachIndexed { index, recipe ->
            recipesInfo[index] = recipesInfo[index].copy(categories = recipeBookDao.getRecipeCategories(recipe.id!!).mapNotNull { it.id })
        }
        return recipesInfo
    }

    override suspend fun createRecipe(recipe: Recipe): Recipe {
        recipe.id = recipeBookDao.addRecipe(recipe.toRecipeEntity()).toInt()
        return recipe
    }

    override suspend fun getRecipeById(recipeId: Int): Recipe  {
        val recipe = recipeBookDao.getRecipeById(recipeId).toRecipe()
        recipe.categories = recipeBookDao.getRecipeCategories(recipe.id!!).mapNotNull { it.id } as ArrayList<Int>
        return recipe
    }
    override suspend fun getRecipeByRemoteId(remoteId: Int): Recipe? {
        val recipe = recipeBookDao.getRecipeByRemoteId(remoteId)?.toRecipe()
        if (recipe != null) {
            recipe.categories = recipeBookDao.getRecipeCategories(recipe.id!!).mapNotNull { it.id } as ArrayList<Int>
        }
        return recipe
    }

    override suspend fun updateRecipe(recipe: Recipe): Recipe {
        recipeBookDao.updateRecipe(recipe.toRecipeEntity())
        return recipe
    }

    override suspend fun deleteRecipe(recipeId: Int) {
        val remoteId = getRecipeById(recipeId).remoteId
        recipeBookDao.deleteRecipe(recipeId)
        if (remoteId != null) syncData.updateData {
            it.toBuilder()
                .addDeletedRecipes(remoteId)
                .build()
        }
    }

    override suspend fun getDeletedRecipeRemoteIds(): List<Int> =
        syncData.data.take(1).first().deletedRecipesList
}