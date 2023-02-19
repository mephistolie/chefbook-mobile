package com.mysty.chefbook.api.auth.data.local.mappers

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mysty.chefbook.api.auth.data.local.dto.TokensProto
import com.mysty.chefbook.core.constants.Strings
import java.io.InputStream
import java.io.OutputStream

internal object TokensSerializer : Serializer<TokensProto> {

    override val defaultValue: TokensProto = TokensProto.newBuilder()
        .setAccessToken(Strings.EMPTY)
        .setRefreshToken(Strings.EMPTY)
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
