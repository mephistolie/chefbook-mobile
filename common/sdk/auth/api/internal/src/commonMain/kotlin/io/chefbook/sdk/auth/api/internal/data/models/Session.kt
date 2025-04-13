package io.chefbook.sdk.auth.api.internal.data.models

import io.chefbook.libs.models.auth.LOCAL_PROFILE_ID

data class Session(
  val profileId: String,
  val accessToken: String? = null,
  val refreshToken: String? = null,
  val expirationTimestamp: String? = null,
  val profileDeletionTimestamp: String? = null,
) {

  val isOnline
    get()= profileId == LOCAL_PROFILE_ID
}
