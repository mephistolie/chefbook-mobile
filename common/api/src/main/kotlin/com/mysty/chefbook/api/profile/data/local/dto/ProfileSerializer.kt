package com.mysty.chefbook.api.profile.data.local.dto

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

internal object ProfileSerializer : Serializer<ProfileProto> {

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
