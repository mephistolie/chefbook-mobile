package com.cactusknights.chefbook.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cactusknights.chefbook.repositories.local.dao.ChefBookDao
import com.cactusknights.chefbook.repositories.local.entities.RecipeEntity

@Database(
    entities = [
        RecipeEntity::class
    ], version = 1, exportSchema = true
)
abstract class ChefBookDatabase: RoomDatabase() {

    abstract fun chefBookDao(): ChefBookDao
}