package com.cactusknights.chefbook.data.repositories

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.TokensProto
import com.cactusknights.chefbook.core.coroutines.CoroutineScopes
import com.cactusknights.chefbook.core.datastore.TokensSerializer
import com.cactusknights.chefbook.domain.entities.common.Tokens
import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@Singleton
class SessionRepo @Inject constructor(
    private val storage: DataStore<TokensProto>,
    private val scopes: CoroutineScopes
) : ISessionRepo {

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
        while (!isInitialized) {
            delay(100)
        }
        return currentTokens
    }

    override fun getAccessToken(): String {
        while (!isInitialized) {
            sleep(100)
        }
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
