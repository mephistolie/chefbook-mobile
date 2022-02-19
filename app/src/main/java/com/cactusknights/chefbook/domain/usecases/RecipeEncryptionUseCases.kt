package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.core.encryption.EncryptionManager
import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.domain.RecipeEncryptionRepo
import com.cactusknights.chefbook.domain.VaultEncryptionRepo
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.crypto.SecretKey
import javax.inject.Inject

class RecipeEncryptionUseCases @Inject constructor(private val repository: RecipeEncryptionRepo, private val manager: EncryptionManager) {

    suspend fun setRecipeKey(recipe: Recipe, key: SecretKey): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.setRecipeKey(recipe.id!!, recipe.remoteId, key)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun decryptRecipeData(recipe: Recipe, encryptedData: ByteArray): Flow<Result<ByteArray>> = flow {
        try {
            emit(Result.Loading)
            val decryptedData = repository.decryptRecipeData(recipe.id!!, recipe.remoteId, encryptedData)
            emit(Result.Success(decryptedData))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun getRecipeKey(recipe: RecipeInfo): Flow<Result<SecretKey>> = flow {
        try {
            emit(Result.Loading)
            val key = repository.getRecipeKey(recipe.id!!, recipe.remoteId)
            emit(Result.Success(key))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun deleteRecipeKey(recipe: Recipe): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.deleteRecipeKey(recipe.id!!, recipe.remoteId)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    fun decryptRecipeData(data: ByteArray, key: SecretKey) : ByteArray {
        return manager.decryptDataBySymmetricKey(data, key)
    }
}