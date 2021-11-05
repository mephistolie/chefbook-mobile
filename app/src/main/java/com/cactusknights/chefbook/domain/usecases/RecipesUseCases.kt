package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.RecipesProvider
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.User

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecipesUseCases @Inject constructor(private val repository: RecipesProvider) {

    fun getRecipes(): Flow<Result<List<Recipe>>> = flow {
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
}