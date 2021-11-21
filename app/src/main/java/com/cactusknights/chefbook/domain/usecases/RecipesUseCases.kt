package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.RecipesDataSource
import com.cactusknights.chefbook.domain.RecipesRepository
import com.cactusknights.chefbook.models.Recipe

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecipesUseCases @Inject constructor(private val repository: RecipesRepository) {

    suspend fun getRecipes(): Flow<Result<ArrayList<Recipe>>> = flow {
        try {
            emit(Result.Loading)
            val recipes = repository.getRecipes()
            emit(Result.Success(recipes))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun addRecipe(recipe: Recipe): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            recipe.id = repository.addRecipe(recipe)
            emit(Result.Success(recipe))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun updateRecipe(recipe: Recipe): Flow<Result<Recipe>> = flow {
        try {
            emit(Result.Loading)
            repository.updateRecipe(recipe)
            emit(Result.Success(recipe))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }


    suspend fun deleteRecipe(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.deleteRecipe(recipe)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setRecipeFavouriteStatus(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setRecipeFavouriteStatus(recipe)
            emit(Result.Success(recipe))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}