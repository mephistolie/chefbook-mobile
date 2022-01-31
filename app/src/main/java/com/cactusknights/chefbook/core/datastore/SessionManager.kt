package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.TokensProto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SessionManager {
    fun getAccessToken(): String
    fun getRefreshToken(): String
    suspend fun saveTokens(newTokens: TokensProto?)
    suspend fun clearTokens()
}

class DataStoreSessionManager @Inject constructor(private val tokens: DataStore<TokensProto>) :
    SessionManager {

    private var savedTokens: TokensProto = TokensProto.getDefaultInstance()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            tokens.data.collect { savedTokens = it }
        }
    }

    override fun getAccessToken(): String = savedTokens.accessToken
    override fun getRefreshToken(): String = savedTokens.refreshToken

    override suspend fun saveTokens(newTokens: TokensProto?) {
        if (newTokens != null) tokens.updateData { newTokens }
        else tokens.updateData { TokensSerializer.defaultValue }
    }

    override suspend fun clearTokens() {
        tokens.updateData { TokensSerializer.defaultValue }
    }
}