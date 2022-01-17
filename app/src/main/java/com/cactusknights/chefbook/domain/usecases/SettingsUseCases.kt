package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.RecipesDataSource
import com.cactusknights.chefbook.domain.RecipesRepository
import com.cactusknights.chefbook.domain.SettingsRepository
import com.cactusknights.chefbook.domain.ShoppingListRepository
import com.cactusknights.chefbook.models.*

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SettingsUseCases @Inject constructor(private val repository: SettingsRepository) {

    fun getSettings() = repository.getSettings()

    suspend fun setShoppingListDefault(isDefault: Boolean): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setShoppingListByDefault(isDefault)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setDataSource(dataSource: DataSource): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setDataSourceType(dataSource)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setTheme(theme: AppTheme):
            Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setAppTheme(theme)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setIcon(icon: AppIcon):
            Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setAppIcon(icon)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}