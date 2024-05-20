package io.chefbook.sdk.profile.impl.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.internal.data.sources.local.LocalProfileSource
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.ProfileSerializer
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.dto.ProfileSerializable
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.dto.toEntity
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.dto.toSerializable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.io.File

internal class LocalProfileSourceImpl(
  private val context: Context,
  private val dispatchers: AppDispatchers,
) : LocalProfileSource {

  private val dataStore = DataStoreFactory.create(
    serializer = ProfileSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

  private val saved get() = dataStore.data.take(1)

  override fun observeProfile() =
    dataStore.data.map(ProfileSerializable::toEntity)

  override suspend fun getProfileInfo(): Result<Profile> =
    Result.success(saved.first().toEntity())

  override suspend fun generateAvatarUploading(): Result<PictureUploading> {
    val path = File(context.filesDir, "profile/avatars/${generateUUID()}").absolutePath
    return Result.success(PictureUploading(
      picturePath = path,
      uploadPath = path,
    ))
  }

  override suspend fun confirmAvatarUploading(path: String): EmptyResult {
    dataStore.updateData { it.copy(avatar = path) }
    return successResult
  }

  override suspend fun deleteAvatar(): EmptyResult {
    dataStore.updateData { it.copy(avatar = null) }
    return successResult
  }

  override suspend fun setName(firstName: String?, lastName: String?): EmptyResult {
    dataStore.updateData { it.copy(firstName = firstName, lastName = lastName) }
    return successResult
  }

  override suspend fun updateProfileCache(update: (Profile) -> Profile) {
    dataStore.updateData { update(it.toEntity()).toSerializable() }
  }

  override suspend fun cacheProfileInfo(info: Profile) {
    dataStore.updateData { info.toSerializable() }
  }

  override suspend fun clearProfileCache() {
    dataStore.updateData { ProfileSerializer.defaultValue }
  }

  companion object {
    private const val DATASTORE_FILE = "profile.json"
  }
}
