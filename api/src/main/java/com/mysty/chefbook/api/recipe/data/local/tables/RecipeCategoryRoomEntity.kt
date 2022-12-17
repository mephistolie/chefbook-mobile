package com.mysty.chefbook.api.recipe.data.local.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeCategoryRoomEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = RecipeRoomEntity::class,
            parentColumns = arrayOf("recipe_id"),
            childColumns = arrayOf("recipe_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryRoomEntity::class,
            parentColumns = arrayOf("category_id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["recipe_id", "category_id"], unique = true)]
)
internal data class RecipeCategoryRoomEntity constructor(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "recipe_id")
    val recipeId: String,

    @ColumnInfo(name = "category_id")
    val categoryId: String
) {
    companion object {
        const val TABLE_NAME = "recipes_categories"
    }
}
