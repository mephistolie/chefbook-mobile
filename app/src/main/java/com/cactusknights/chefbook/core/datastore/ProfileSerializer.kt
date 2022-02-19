package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.ProfileProto
import com.cactusknights.chefbook.SettingsProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object ProfileSerializer : Serializer<ProfileProto> {

    override val defaultValue: ProfileProto = ProfileProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProfileProto {
        return try {
            ProfileProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ProfileProto, output: OutputStream) = t.writeTo(output)
}