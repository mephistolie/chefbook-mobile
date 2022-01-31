package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.ShoppingListProto
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.Timestamp
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
object ShoppingListSerializer : Serializer<ShoppingListProto> {

    override val defaultValue: ShoppingListProto = ShoppingListProto.newBuilder()
        .setTimestamp(Date().time)
        .build()

    override suspend fun readFrom(input: InputStream): ShoppingListProto {
        return try {
            ShoppingListProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ShoppingListProto, output: OutputStream) = t.writeTo(output)
}