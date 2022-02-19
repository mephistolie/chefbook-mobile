package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.domain.RecipeBookSyncRepo
import com.cactusknights.chefbook.domain.RecipeCrudRepo
import com.cactusknights.chefbook.domain.RecipeInteractionRepo
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class RecipesUseCases @Inject constructor(
    private val recipeBookRepo: RecipeBookSyncRepo,
    private val recipeCrudRepo: RecipeCrudRepo,
    private val recipeInteractionRepo: RecipeInteractionRepo,
) {

    suspend fun listenToRecipes() = recipeBookRepo.listenToRecipeBook()

    suspend fun getRecipeBook(): Flow<Result<List<RecipeInfo>>> = flow {
        try {
            emit(Result.Loading)
            val recipes = recipeBookRepo.getRecipeBook()
            emit(Result.Success(recipes))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun addRecipe(recipe: DecryptedRecipe): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            val committedRecipe = recipeCrudRepo.createRecipe(recipe)
            emit(Result.Success(committedRecipe))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun getRecipeById(recipeId: Int): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            val recipes = recipeCrudRepo.getRecipeById(recipeId)
            emit(Result.Success(recipes))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun getRecipeByRemoteId(remoteId: Int): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            val recipes = recipeCrudRepo.getRecipeByRemoteId(remoteId)
            emit(Result.Success(recipes))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun updateRecipe(recipe: DecryptedRecipe): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            recipeCrudRepo.updateRecipe(recipe)
            emit(Result.Success(recipe))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }


    suspend fun deleteRecipe(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            recipeCrudRepo.deleteRecipe(recipe)
            emit(Result.Success(null))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun setRecipeFavouriteStatus(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            recipeInteractionRepo.setRecipeFavouriteStatus(recipe)
            emit(Result.Success(recipe))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun setRecipeLikeStatus(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            recipeInteractionRepo.setRecipeLikeStatus(recipe)
            emit(Result.Success(recipe))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun setRecipeCategories(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            recipeInteractionRepo.setRecipeCategories(recipe)
            emit(Result.Success(recipe))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}