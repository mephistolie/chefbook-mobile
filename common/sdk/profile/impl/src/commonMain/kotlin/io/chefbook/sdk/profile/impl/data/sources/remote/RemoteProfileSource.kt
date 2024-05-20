package io.chefbook.sdk.profile.impl.data.sources.remote

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.profile.api.internal.data.sources.ProfileSource

internal interface RemoteProfileSource : ProfileSource {

  suspend fun checkNicknameAvailability(nickname: String): Result<Boolean>

  suspend fun setNickname(nickname: String): EmptyResult

  suspend fun setDescription(description: String?): EmptyResult

  suspend fun requestProfileDeletion(password: String, deleteSharedData: Boolean): EmptyResult

  suspend fun cancelProfileDeletion(): EmptyResult
}
