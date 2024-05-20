package io.chefbook.sdk.profile.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

  fun observeProfile(): Flow<Profile?>

  suspend fun getProfile(): Result<Profile>

  suspend fun getProfileId(): String

  suspend fun refreshProfile(): EmptyResult

  suspend fun uploadAvatar(path: String): EmptyResult

  suspend fun deleteAvatar(): EmptyResult

  suspend fun checkNicknameAvailability(nickname: String): Result<Boolean>

  suspend fun setNickname(nickname: String): EmptyResult

  suspend fun setName(firstName: String?, lastName: String?): EmptyResult

  suspend fun setDescription(description: String?): EmptyResult

  suspend fun requestProfileDeletion(password: String, deleteSharedData: Boolean): EmptyResult

  suspend fun cancelProfileDeletion(): EmptyResult

  suspend fun clearLocalData()
}
