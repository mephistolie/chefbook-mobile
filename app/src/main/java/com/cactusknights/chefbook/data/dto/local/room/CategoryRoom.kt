package com.cactusknights.chefbook.data.dto.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cactusknights.chefbook.data.dto.local.room.CategoryRoom.Companion.TABLE_NAME
import com.cactusknights.chefbook.domain.entities.category.Category
import java.util.*

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["category_id"], unique = true)])
data class CategoryRoom constructor(

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

fun CategoryRoom.toEntity() =
    Category(
        id = id,
        name = name,
        cover = cover
    )

fun Category.toRoom() =
    CategoryRoom(
        id = id,
        name = name,
        cover = cover
    )
