package com.mysty.chefbook.api.auth.data

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.entities.tokens.Tokens

interface IAuthSource {
    suspend fun signUp(email: String, password: String): SimpleAction
    suspend fun signIn(email: String, password: String): ActionStatus<Tokens>
    suspend fun refreshTokens(refreshToken: String): ActionStatus<Tokens>
    suspend fun signOut(refreshToken: String): SimpleAction
}
