package com.mysty.chefbook.api.auth.data.remote

import com.mysty.chefbook.api.auth.data.IAuthSource
import com.mysty.chefbook.api.auth.data.remote.api.AuthApi
import com.mysty.chefbook.api.auth.data.remote.dto.CredentialsBody
import com.mysty.chefbook.api.auth.data.remote.dto.RefreshTokenRequest
import com.mysty.chefbook.api.auth.data.remote.dto.toEntity
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.entities.tokens.Tokens
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.common.network.dto.isSuccess
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import java.util.UUID

internal class RemoteAuthSource(
    private val api: AuthApi,
) : IAuthSource {

    override suspend fun signUp(email: String, password: String) =
        api.signUp(CredentialsBody(email = email, password = password, userId = UUID.randomUUID().toString()))
            .toActionStatus().asEmpty()

    override suspend fun signIn(email: String, password: String): ActionStatus<Tokens> {
        val result = api.signIn(CredentialsBody(email = email, password = password))
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().toEntity())
    }

    override suspend fun refreshTokens(refreshToken: String): ActionStatus<Tokens> {
        val result = api.refreshSession(RefreshTokenRequest(refreshToken))
        return if (result.isSuccess()) DataResult(result.body().toEntity()) else result.toActionStatus().asFailure()
    }

    override suspend fun signOut(refreshToken: String): SimpleAction =
        api.signOut(RefreshTokenRequest(refreshToken)).toActionStatus().asEmpty()

}
