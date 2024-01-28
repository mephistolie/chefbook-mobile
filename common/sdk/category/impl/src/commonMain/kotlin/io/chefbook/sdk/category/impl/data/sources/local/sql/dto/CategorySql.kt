package io.chefbook.sdk.category.impl.data.sources.local.sql.dto

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.database.api.internal.Category as CategorySql

fun CategorySql.toEntity() =
  Category(
    id = category_id,
    name = name,
    emoji = emoji,
  )

fun Category.toDto(ownerId: String) =
  CategorySql(
    category_id = id,
    owner_id = ownerId,
    name = name,
    emoji = emoji,
  )
