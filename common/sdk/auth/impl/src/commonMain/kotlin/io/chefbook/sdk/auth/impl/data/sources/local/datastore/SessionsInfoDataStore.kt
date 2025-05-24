package io.chefbook.sdk.auth.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.auth.impl.data.sources.local.dto.SessionsInfo
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory

internal interface SessionsInfoDataStore : DataStore<SessionsInfo>

internal class SessionsInfoDataStoreImpl(
  factory: ChefBookDataStoreFactory,
) : SessionsInfoDataStore, DataStore<SessionsInfo> by factory.create(
  fileName = "sessions.json",
  serializer = SessionsSerializer,
)
