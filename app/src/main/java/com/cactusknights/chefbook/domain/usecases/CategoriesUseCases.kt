package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.domain.CategoriesCrudRepo
import com.cactusknights.chefbook.domain.CategoriesSyncRepo
import com.cactusknights.chefbook.models.Category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CategoriesUseCases @Inject constructor(
    private val sync: CategoriesSyncRepo,
    private val crud: CategoriesCrudRepo,
) {

    suspend fun listenToCategories() = sync.listenToCategories()

    suspend fun getCategoriesList(): Flow<Result<List<Category>>> = flow {
        try {
            emit(Result.Loading)
            val categories = sync.getCategories()
            emit(Result.Success(categories))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun addCategory(category: Category): Flow<Result<Category>> = flow {
        try {
            emit(Result.Loading)
            category.remoteId = crud.addCategory(category)
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
            crud.updateCategory(category)
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
            crud.deleteCategory(category)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}