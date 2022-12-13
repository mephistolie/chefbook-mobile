package com.cactusknights.chefbook.data.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.ShoppingListProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object ShoppingListSerializer : Serializer<ShoppingListProto> {

    override val defaultValue: ShoppingListProto = ShoppingListProto.newBuilder()
        .setTimestamp(0)
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
