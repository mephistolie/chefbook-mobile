package io.chefbook.sdk.profile.impl.data.sources.remote

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.profile.impl.data.sources.ProfileSource

internal interface RemoteProfileSource : ProfileSource {
  suspend fun changeName(username: String): EmptyResult
  suspend fun deleteAvatar(): EmptyResult
}
