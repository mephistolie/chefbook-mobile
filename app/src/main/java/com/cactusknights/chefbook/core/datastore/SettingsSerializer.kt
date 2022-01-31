package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.SettingsProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object SettingsSerializer : Serializer<SettingsProto> {

    override val defaultValue: SettingsProto = SettingsProto.newBuilder()
        .setDataSource(SettingsProto.DataSource.LOCAL)
        .setUserType(SettingsProto.UserType.OFFLINE)
        .setDefaultTab(SettingsProto.Tabs.RECIPES)
        .setAppTheme(SettingsProto.AppTheme.SYSTEM)
        .setAppIcon(SettingsProto.AppIcon.STANDARD)
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