package io.chefbook.sdk.collection.impl.data.sources.local.sql.dto

import io.chefbook.libs.models.visibility.Visibility
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.api.external.domain.entities.Contributor
import io.chefbook.sdk.database.api.internal.collection.SelectAll
import kotlinx.serialization.encodeToString
import io.chefbook.sdk.database.api.internal.Collections as CollectionSql
import kotlinx.serialization.json.Json

fun SelectAll.toEntity() =
  Collection(
    id = collectionId.orEmpty(),
    name = name.orEmpty(),
    visibility = Visibility.deserialize(visibility),
    contributors = Json.decodeFromString<List<ContributorSerializable>>(contributors.orEmpty())
      .map(ContributorSerializable::toEntity),
    recipesCount = recipesCount?.toInt() ?: 0,
  )

fun CollectionSql.toEntity() =
  Collection(
    id = collectionId,
    name = name,
    visibility = Visibility.deserialize(visibility),
    contributors = Json.decodeFromString<List<ContributorSerializable>>(contributors)
      .map(ContributorSerializable::toEntity),
    recipesCount = recipesCount.toInt(),
  )

fun Collection.toDto() =
  CollectionSql(
    collectionId = id,
    name = name,
    visibility = visibility.serialize(),
    contributors = Json.encodeToString(contributors.map(Contributor::toSerializable)),
    recipesCount = recipesCount.toLong(),
  )
