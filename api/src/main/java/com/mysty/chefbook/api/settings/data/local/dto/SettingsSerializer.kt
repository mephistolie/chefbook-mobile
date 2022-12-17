package com.mysty.chefbook.api.settings.data.local.dto

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mysty.chefbook.core.constants.Strings
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

internal object SettingsSerializer : Serializer<SettingsProto> {

    private var appLanguage = Strings.EMPTY

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
