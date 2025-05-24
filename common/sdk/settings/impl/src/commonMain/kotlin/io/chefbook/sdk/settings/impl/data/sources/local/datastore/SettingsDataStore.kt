package io.chefbook.sdk.settings.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.settings.impl.data.sources.local.datastore.dto.SettingsSerializable

internal interface SettingsDataStore : DataStore<SettingsSerializable>

internal class SettingsDataStoreImpl(
  factory: ChefBookDataStoreFactory
) : SettingsDataStore, DataStore<SettingsSerializable> by factory.create(
  fileName = "settings.json",
  serializer = SettingsSerializer,
)
