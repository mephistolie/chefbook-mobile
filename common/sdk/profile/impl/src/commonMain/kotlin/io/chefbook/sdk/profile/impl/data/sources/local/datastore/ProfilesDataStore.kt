package io.chefbook.sdk.profile.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.profile.impl.data.sources.common.dto.ProfileSerializable

internal interface ProfilesDataStore : DataStore<Map<String, ProfileSerializable>>

class ProfilesDataStoreImpl(
  factory: ChefBookDataStoreFactory,
) : ProfilesDataStore, DataStore<Map<String, ProfileSerializable>> by factory.create(
  fileName = "profiles.json",
  serializer = ProfilesSerializer,
)
