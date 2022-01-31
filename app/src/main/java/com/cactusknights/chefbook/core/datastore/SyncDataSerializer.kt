package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.SyncDataProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object SyncDataSerializer : Serializer<SyncDataProto> {

    override val defaultValue: SyncDataProto = SyncDataProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SyncDataProto {
        return try {
            SyncDataProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: SyncDataProto, output: OutputStream) = t.writeTo(output)
}