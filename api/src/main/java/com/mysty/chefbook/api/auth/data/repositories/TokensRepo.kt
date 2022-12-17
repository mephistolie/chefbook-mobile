package com.mysty.chefbook.api.auth.data.repositories

import androidx.datastore.core.DataStore
import com.mysty.chefbook.api.auth.data.local.dto.TokensProto
import com.mysty.chefbook.api.auth.data.local.mappers.TokensSerializer
import com.mysty.chefbook.api.auth.domain.ITokensRepo
import com.mysty.chefbook.api.common.entities.tokens.Tokens
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import java.lang.Thread.sleep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

private const val INIT_TIMEOUT = 100L

class TokensRepo(
    private val storage: DataStore<TokensProto>,
    private val scopes: CoroutineScopes
) : ITokensRepo {

    private val currentTokens: MutableStateFlow<Tokens?> = MutableStateFlow(null)
    private var isInitialized = false

    init {
        initSession()
    }

    private fun initSession() {
        scopes.repository.launch {
            val tokens = storage.data.first()
            if (tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
                currentTokens.emit(Tokens(tokens.accessToken, tokens.refreshToken))
            }
            isInitialized = true
        }
    }

    override suspend fun observeSession(): MutableStateFlow<Tokens?> {
        while (!isInitialized) delay(INIT_TIMEOUT)
        return currentTokens
    }

    override fun getAccessToken(): String {
        while (!isInitialized) sleep(INIT_TIMEOUT)
        return currentTokens.value?.accessToken.orEmpty()
    }

    override suspend fun getRefreshToken(): String = currentTokens.value?.refreshToken.orEmpty()

    override suspend fun saveTokens(tokens: Tokens) {
        storage.updateData { data ->
            data.toBuilder()
                .setAccessToken(tokens.accessToken)
                .setRefreshToken(tokens.refreshToken)
                .build()
        }
        currentTokens.emit(tokens)
        Timber.i("Tokens saved")
    }

    override suspend fun clearTokens() {
        storage.updateData { TokensSerializer.defaultValue }
        currentTokens.emit(null)
        Timber.i("Tokens cleared")
    }

}
