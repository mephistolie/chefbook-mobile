package com.mysty.chefbook.api.auth.data.repositories

import com.mysty.chefbook.api.auth.data.IAuthSource
import com.mysty.chefbook.api.auth.domain.IAuthRepo
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.entities.tokens.Tokens

internal class AuthRepo(
    private val remote: IAuthSource,
) : IAuthRepo {

    override suspend fun signUp(email: String, password: String): SimpleAction =
        remote.signUp(email, password)

    override suspend fun signIn(email: String, password: String): ActionStatus<Tokens> =
        remote.signIn(email, password)

    override suspend fun refreshTokens(refreshToken: String): ActionStatus<Tokens> =
        remote.refreshTokens(refreshToken)

    override suspend fun signOut(refreshToken: String): SimpleAction =
        remote.signOut(refreshToken)

}
