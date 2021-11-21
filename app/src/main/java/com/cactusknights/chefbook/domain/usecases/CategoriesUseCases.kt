package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.CategoriesRepository
import com.cactusknights.chefbook.domain.RecipesDataSource
import com.cactusknights.chefbook.domain.RecipesRepository
import com.cactusknights.chefbook.domain.ShoppingListRepository
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CategoriesUseCases @Inject constructor(private val repository: CategoriesRepository) {

    suspend fun getCategoriesList(): Flow<Result<ArrayList<Category>>> = flow {
        try {
            emit(Result.Loading)
            val shoppingList = repository.getCategories()
            emit(Result.Success(shoppingList))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun addCategory(category: Category): Flow<Result<Category>> = flow {
        try {
            emit(Result.Loading)
            category.id = repository.addCategory(category)
            emit(Result.Success(category))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun updateCategory(category: Category): Flow<Result<Category>> = flow {
        try {
            emit(Result.Loading)
            repository.updateCategory(category)
            emit(Result.Success(category))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }


    suspend fun deleteCategory(category: Category): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.deleteCategory(category)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}