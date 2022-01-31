package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.core.datastore.SettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SettingsUseCases @Inject constructor(private val repository: SettingsManager) {

    suspend fun listenToSettings() = repository.listenToSettings()
    suspend fun getSettings() = repository.getSettings()

    suspend fun setDefaultTab(tab: SettingsProto.Tabs): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setDefaultTab(tab)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setDataSource(dataSource: SettingsProto.DataSource): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setDataSourceType(dataSource)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setTheme(theme: SettingsProto.AppTheme):
            Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setAppTheme(theme)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun setIcon(icon: SettingsProto.AppIcon):
            Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setAppIcon(icon)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}