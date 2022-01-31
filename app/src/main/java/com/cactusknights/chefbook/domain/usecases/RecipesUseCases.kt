package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.domain.RecipesRepository
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Recipe

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecipesUseCases @Inject constructor(private val repository: RecipesRepository) {

    suspend fun listenToRecipes() = repository.listenToUserRecipes()

    suspend fun getRecipes(): Flow<Result<List<Recipe>>> = flow {
        try {
            emit(Result.Loading)
            val recipes = repository.getRecipes()
            emit(Result.Success(recipes))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun addRecipe(recipe: DecryptedRecipe): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            val committedRecipe = repository.addRecipe(recipe)
            emit(Result.Success(committedRecipe))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun getRecipe(recipeId: Int): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            val recipes = repository.getRecipe(recipeId)
            emit(Result.Success(recipes))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun getRecipeByRemoteId(remoteId: Int): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            val recipes = repository.getRecipeByRemoteId(remoteId)
            emit(Result.Success(recipes))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun updateRecipe(recipe: DecryptedRecipe): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            repository.updateRecipe(recipe)
            emit(Result.Success(recipe))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }


    suspend fun deleteRecipe(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.deleteRecipe(recipe)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setRecipeFavouriteStatus(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setRecipeFavouriteStatus(recipe)
            emit(Result.Success(recipe))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setRecipeLikeStatus(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setRecipeLikeStatus(recipe)
            emit(Result.Success(recipe))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setRecipeCategories(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setRecipeCategories(recipe)
            emit(Result.Success(recipe))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}