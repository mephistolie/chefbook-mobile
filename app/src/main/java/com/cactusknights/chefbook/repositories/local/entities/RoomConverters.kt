package com.cactusknights.chefbook.repositories.local.entities

import androidx.room.TypeConverter
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.Visibility
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
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
            return visibility.toString().lowercase()
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
            val type: Type = object : TypeToken<MarkdownString>() {}.type
            return Gson().fromJson(data, type)
        }
    }
}