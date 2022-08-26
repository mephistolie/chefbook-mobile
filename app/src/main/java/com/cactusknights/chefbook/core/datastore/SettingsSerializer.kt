package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.SettingsProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
object SettingsSerializer : Serializer<SettingsProto> {

    private var appLanguage = ""

    init {
        appLanguage = Locale.getDefault().language
    }

    override val defaultValue: SettingsProto = SettingsProto.newBuilder()
        .setAppMode(SettingsProto.AppMode.ONLINE)
        .setAppTheme(SettingsProto.AppTheme.SYSTEM)
        .setAppIcon(SettingsProto.AppIcon.STANDARD)
        .setDefaultTab(SettingsProto.Tab.RECIPE_BOOK)
        .setIsFirstAppLaunch(true)
        .setDefaultRecipeLanguage(appLanguage)
        .addOnlineRecipesLanguages(appLanguage)
        .build()

    override suspend fun readFrom(input: InputStream): SettingsProto {
        return try {
            SettingsProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: SettingsProto, output: OutputStream) = t.writeTo(output)
}
