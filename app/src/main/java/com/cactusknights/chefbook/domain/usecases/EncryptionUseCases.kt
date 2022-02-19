package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.core.encryption.EncryptionManager
import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.domain.VaultEncryptionRepo
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.crypto.SecretKey
import javax.inject.Inject

class EncryptionUseCases @Inject constructor(private val repository: VaultEncryptionRepo, private val manager: EncryptionManager) {

    suspend fun listenToUnlockedState(): StateFlow<Boolean> = repository.listenToUnlockedState()

    suspend fun getEncryptionState(): Flow<Result<EncryptionState>> = flow {
        try {
            emit(Result.Loading)
            val state = repository.getEncryptionState()
            emit(Result.Success(state))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }


    suspend fun createEncryptedVault(password: String): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.createEncryptedVault(password)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun unlockEncryptedStorage(password: String): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.unlockEncryptedVault(password)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun lockEncryptedStorage(): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.lockEncryptedVault()
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun deleteEncryptedStorage(): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.deleteEncryptedVault()
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}