package com.mysty.chefbook.api.auth.domain

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.entities.tokens.Tokens
import kotlinx.coroutines.flow.MutableStateFlow

internal interface ITokensRepo {
    suspend fun observeSession(): MutableStateFlow<Tokens?>
    fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(tokens: Tokens)
    suspend fun clearTokens()
}

internal interface IAuthRepo {
    suspend fun signUp(email: String, password: String): SimpleAction
    suspend fun signIn(email: String, password: String): ActionStatus<Tokens>
    suspend fun refreshTokens(refreshToken: String): ActionStatus<Tokens>
    suspend fun signOut(refreshToken: String): SimpleAction
}
