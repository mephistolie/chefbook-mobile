package io.chefbook.sdk.collection.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.impl.data.sources.local.sql.dto.toDto
import io.chefbook.sdk.collection.impl.data.sources.local.sql.dto.toEntity
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.database.api.internal.collection.SelectAll

internal class LocalCollectionSourceImpl(
  private val profileId: String,
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalCollectionSource {

  private val queries = database.collectionQueries

  override suspend fun getCollections(): Result<List<Collection>> =
    safeQueryResult {
      queries.selectAll(profileId).executeAsList().map(SelectAll::toEntity)
    }

  override suspend fun getCollection(
    collectionId: String,
  ): Result<Collection> = safeQueryResult {
    queries.select(collectionId).executeAsOne().toEntity()
  }

  override suspend fun insertCollection(
    collection: Collection,
  ): Result<String> = safeQueryResult {
    queries.insert(collection.toDto())
    return@safeQueryResult collection.id
  }

  override suspend fun updateCollection(
    collectionId: String,
    input: CollectionInput,
  ): EmptyResult = safeQueryResult {
    queries.update(
      collectionId = collectionId,
      name = input.name,
      visibility = input.visibility.serialize(),
    )
  }

  override suspend fun deleteCollection(collectionId: String): EmptyResult =
    safeQueryResult { queries.delete(collectionId) }

  override suspend fun clearUnusedCollections(): EmptyResult =
    safeQueryResult { queries.clearUnused() }
}
