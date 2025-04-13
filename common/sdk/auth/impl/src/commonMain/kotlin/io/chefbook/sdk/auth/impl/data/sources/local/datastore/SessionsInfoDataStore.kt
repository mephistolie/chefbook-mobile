package io.chefbook.sdk.auth.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.auth.impl.data.sources.local.dto.SessionsInfo
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory

class SessionsInfoDataStore(
  factory: ChefBookDataStoreFactory,
): DataStore<SessionsInfo> by factory.create(
  fileName = "sessions.json",
  serializer = SessionsSerializer,
)
