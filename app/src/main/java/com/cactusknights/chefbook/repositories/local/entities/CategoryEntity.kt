package com.cactusknights.chefbook.repositories.local.entities

import androidx.room.*
import com.cactusknights.chefbook.common.RoomConverters
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.repositories.local.entities.CategoryEntity.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["remoteId"], unique = true)])
@TypeConverters(RoomConverters::class)
data class CategoryEntity constructor(

    @PrimaryKey(autoGenerate = true) @SerializedName("category_id")
    var id: Int? = null,

    @SerializedName("remote_id")
    var remoteId: Int? = null,

    var name: String,
    var cover: String
) {
    companion object {
        const val TABLE_NAME = "categories"
    }
}

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        remoteId = remoteId,
        name = name,
        cover = cover
    )
}

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        remoteId = remoteId,
        name = name,
        cover = cover
    )
}