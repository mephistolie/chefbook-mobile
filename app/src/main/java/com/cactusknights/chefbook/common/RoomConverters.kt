package com.cactusknights.chefbook.common

import androidx.room.TypeConverter
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.Visibility
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class RoomConverters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }

        @TypeConverter
        @JvmStatic
        fun visibilityToString(visibility: Visibility): String {
            return visibility.toString()
        }

        @TypeConverter
        @JvmStatic
        fun stringToVisibility(visibility: String): Visibility {
            return Visibility.valueOf(visibility)
        }

        fun fromMarkdownString(data: ArrayList<MarkdownString>): String {
            return Gson().toJson(data)
        }

        fun toMarkdownString(data: String): ArrayList<MarkdownString> {
            val type: Type = object : TypeToken<ArrayList<MarkdownString>>() {}.type
            return Gson().fromJson(data, type)
        }
    }
}