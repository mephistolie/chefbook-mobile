package io.chefbook.sdk.profile.impl.data.sources.local

import io.chefbook.libs.io.IOProvider
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.impl.data.sources.common.ProfileSource
import io.chefbook.sdk.profile.impl.data.sources.common.dto.ProfileSerializable
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.ProfilesDataStore
import kotlinx.coroutines.flow.first

internal class LocalProfileSourceImpl(
  private val profileId: String,
  private val dataStore: ProfilesDataStore,
  io: IOProvider,
) : ProfileSource {

  private val avatarsDir by lazy { io.filesDir.resolve("profiles/$profileId/avatars") }

  override suspend fun getProfile(): Result<Profile> {
    val profile = dataStore.data.first()[profileId]
      ?: return Result.failure(NotFoundException())
    return Result.success(profile.toEntity())
  }

  override suspend fun generateAvatarUploading(): Result<PictureUploading> {
    val path = avatarsDir.resolve(generateUUID()).toString()
    return Result.success(
      PictureUploading(
        picturePath = path,
        uploadPath = path,
      )
    )
  }

  override suspend fun confirmAvatarUploading(avatarId: String): EmptyResult {
    val path = avatarsDir.resolve(avatarId).toString()
    return updateProfile { it.copy(avatar = path) }
  }

  override suspend fun deleteAvatar() =
    updateProfile { it.copy(avatar = null) }

  override suspend fun setName(firstName: String?, lastName: String?) =
    updateProfile { it.copy(firstName = firstName, lastName = lastName) }

  override suspend fun setNickname(nickname: String) =
    updateProfile { it.copy(nickname = nickname) }

  override suspend fun setDescription(description: String?) =
    updateProfile { it.copy(description = description) }

  private suspend fun updateProfile(block: (ProfileSerializable) -> ProfileSerializable): EmptyResult {
    dataStore.updateData { profiles ->
      val profile = profiles[profileId] ?: return@updateData profiles
      profiles + (profileId to block(profile))
    }
    return successResult
  }
}
