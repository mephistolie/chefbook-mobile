package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.Serializer
import com.cactusknights.chefbook.TokensProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object TokensSerializer : Serializer<TokensProto> {

    override val defaultValue: TokensProto = TokensProto.newBuilder()
        .setAccessToken("")
        .setRefreshToken("")
        .build()

    override suspend fun readFrom(input: InputStream): TokensProto {
        return try {
            TokensProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: TokensProto, output: OutputStream) = t.writeTo(output)
}
