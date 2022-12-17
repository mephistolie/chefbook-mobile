package com.mysty.chefbook.api.recipe.data.datastore

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mysty.chefbook.api.recipes.data.local.dto.LatestRecipesProto
import java.io.InputStream
import java.io.OutputStream

internal object LatestRecipesSerializer : Serializer<LatestRecipesProto> {

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
