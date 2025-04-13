package io.chefbook.sdk.profile.impl.data.sources.common

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import io.chefbook.sdk.profile.api.external.domain.entities.Profile

interface ProfileSource {

  suspend fun getProfile(): Result<Profile>

  suspend fun generateAvatarUploading(): Result<PictureUploading>

  suspend fun confirmAvatarUploading(avatarId: String): EmptyResult

  suspend fun deleteAvatar(): EmptyResult

  suspend fun setName(firstName: String?, lastName: String?): EmptyResult

  suspend fun setNickname(nickname: String): EmptyResult

  suspend fun setDescription(description: String?): EmptyResult
}
