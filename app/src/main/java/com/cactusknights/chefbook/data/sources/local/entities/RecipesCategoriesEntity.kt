package com.cactusknights.chefbook.data.sources.local.entities

import androidx.room.*
import com.cactusknights.chefbook.core.room.RoomConverters
import com.cactusknights.chefbook.data.sources.local.entities.RecipesCategoriesEntity.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recipeId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["recipeId", "categoryId"], unique = true)])
@TypeConverters(RoomConverters::class)
data class RecipesCategoriesEntity constructor(

    @PrimaryKey(autoGenerate = true)
    var Id: Int? = null,

    @SerializedName("recipe_id")
    var recipeId: Int,

    @SerializedName("category_id")
    var categoryId: Int
) {
    companion object {
        const val TABLE_NAME = "recipes_categories"
    }
}