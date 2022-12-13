package com.cactusknights.chefbook.data.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.LatestRecipesProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object LatestRecipesSerializer : Serializer<LatestRecipesProto> {

    override val defaultValue: LatestRecipesProto = LatestRecipesProto.newBuilder()
        .clearLatestRecipes()
        .build()

    override suspend fun readFrom(input: InputStream): LatestRecipesProto {
        return try {
            LatestRecipesProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: LatestRecipesProto, output: OutputStream) = t.writeTo(output)
}
