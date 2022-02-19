package com.cactusknights.chefbook.core.room

import androidx.room.TypeConverter
import com.cactusknights.chefbook.models.CookingStep
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.models.Visibility
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

object RoomConverters {
    @TypeConverter
    @JvmStatic
    fun longToDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    @JvmStatic
    fun dateToLong(date: Date?): Long? = date?.time

    @TypeConverter
    @JvmStatic
    fun visibilityToString(visibility: Visibility) = visibility.toString()

    @TypeConverter
    @JvmStatic
    fun stringToVisibility(visibility: String) = Visibility.valueOf(visibility.uppercase())
}