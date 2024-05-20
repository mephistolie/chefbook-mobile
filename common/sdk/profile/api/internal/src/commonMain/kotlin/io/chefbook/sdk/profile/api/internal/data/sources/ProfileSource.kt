package io.chefbook.sdk.profile.api.internal.data.sources

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import io.chefbook.sdk.profile.api.external.domain.entities.Profile

interface ProfileSource {
  suspend fun getProfileInfo(): Result<Profile>

  suspend fun generateAvatarUploading(): Result<PictureUploading>

  suspend fun confirmAvatarUploading(path: String): EmptyResult

  suspend fun deleteAvatar(): EmptyResult

  suspend fun setName(firstName: String?, lastName: String?): EmptyResult
}
