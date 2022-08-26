package com.cactusknights.chefbook.data.dto.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cactusknights.chefbook.data.dto.local.room.RecipesCategoriesRoom.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = RecipeRoom::class,
            parentColumns = arrayOf("recipe_id"),
            childColumns = arrayOf("recipe_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryRoom::class,
            parentColumns = arrayOf("category_id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["recipe_id", "category_id"], unique = true)]
)
data class RecipesCategoriesRoom constructor(

    @PrimaryKey(autoGenerate = true)
    var Id: Int? = null,

    @ColumnInfo(name = "recipe_id")
    var recipeId: Int,

    @ColumnInfo(name = "category_id")
    var categoryId: Int
) {
    companion object {
        const val TABLE_NAME = "recipes_categories"
    }
}
