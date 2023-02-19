package com.mysty.chefbook.api.category.data.local.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity.Companion.TABLE_NAME
import com.mysty.chefbook.api.category.domain.entities.Category
import java.util.UUID

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["category_id"], unique = true)])
internal data class CategoryRoomEntity constructor(

    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cover")
    val cover: String? = null,

) {
    companion object {
        const val TABLE_NAME = "categories"
    }
}

internal fun CategoryRoomEntity.toEntity() =
    Category(
        id = id,
        name = name,
        cover = cover
    )

internal fun Category.toRoom() =
    CategoryRoomEntity(
        id = id,
        name = name,
        cover = cover
    )
