package io.chefbook.sdk.auth.impl.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.TokensSerializer
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.TokensSerializable
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.toModel
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.toSerializable
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class TokensDataSourceImpl(
  context: Context,
) : TokensDataSource {

  private val dataStore = DataStoreFactory.create(
    serializer = TokensSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

  override fun observeTokens() =
    dataStore.data.map(TokensSerializable::toModel)

  override suspend fun getTokens() =
    observeTokens().first()

  override suspend fun updateTokens(tokens: BearerTokens) {
    dataStore.updateData { tokens.toSerializable() }
    Logger.i("Tokens updated")
  }

  override suspend fun clearTokens() {
    dataStore.updateData { TokensSerializer.defaultValue }
    Logger.i("Tokens cleared")
  }

  companion object {
    private const val DATASTORE_FILE = "tokens.json"
  }
}
