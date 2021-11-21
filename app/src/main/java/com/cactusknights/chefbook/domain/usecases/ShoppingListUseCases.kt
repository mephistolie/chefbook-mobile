package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.RecipesDataSource
import com.cactusknights.chefbook.domain.RecipesRepository
import com.cactusknights.chefbook.domain.ShoppingListRepository
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ShoppingListUseCases @Inject constructor(private val repository: ShoppingListRepository) {

    suspend fun getShoppingList(): Flow<Result<ArrayList<Selectable<String>>>> = flow {
        try {
            emit(Result.Loading)
            val shoppingList = repository.getShoppingList()
            emit(Result.Success(shoppingList))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setShoppingList(shoppingList: ArrayList<Selectable<String>>):
            Flow<Result<ArrayList<Selectable<String>>>> = flow {
        try {
            emit(Result.Loading)
            repository.setShoppingList(shoppingList)
            emit(Result.Success(shoppingList))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun addToShoppingList(purchases: ArrayList<Selectable<String>>):
            Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.addToShoppingList(purchases)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}