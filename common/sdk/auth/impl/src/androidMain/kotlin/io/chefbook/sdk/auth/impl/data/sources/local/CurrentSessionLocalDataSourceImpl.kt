package io.chefbook.sdk.auth.impl.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.SessionSerializer
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.SessionSerializable
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.toModel
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.toSerializable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class CurrentSessionLocalDataSourceImpl(
  context: Context,
) : CurrentSessionLocalDataSource {

  private val dataStore = DataStoreFactory.create(
    serializer = SessionSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

  override fun observeSessionInfo() =
    dataStore.data.map(SessionSerializable::toModel)

  override suspend fun getSessionInfo() =
    observeSessionInfo().first()

  override suspend fun updateSession(tokens: Session) {
    dataStore.updateData { tokens.toSerializable() }
    Logger.i("Tokens updated")
  }

  override suspend fun clearTokens() {
    dataStore.updateData { SessionSerializer.defaultValue }
    Logger.i("Tokens cleared")
  }

  companion object {
    private const val DATASTORE_FILE = "tokens.json"
  }
}
