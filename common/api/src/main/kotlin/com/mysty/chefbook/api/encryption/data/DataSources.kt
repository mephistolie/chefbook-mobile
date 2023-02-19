package com.mysty.chefbook.api.encryption.data

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction


internal interface IEncryptionSource {
    suspend fun setUserKey(data: ByteArray): SimpleAction
    suspend fun deleteUserKey(): SimpleAction
    suspend fun getRecipeKey(recipeId: String): ActionStatus<ByteArray>
    suspend fun setRecipeKey(recipeId: String, key: ByteArray): SimpleAction
    suspend fun deleteRecipeKey(recipeId: String): SimpleAction
}

internal interface ILocalEncryptionSource: IEncryptionSource {
    suspend fun getUserKey(): ActionStatus<ByteArray>
}

internal interface IRemoteEncryptionSource: IEncryptionSource {
    suspend fun getUserKeyLink(): ActionStatus<String>
    suspend fun getUserKey(link: String): ActionStatus<ByteArray>
}