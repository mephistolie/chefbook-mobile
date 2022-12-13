package com.cactusknights.chefbook.data.datasources.remote

import com.cactusknights.chefbook.data.IAuthSource
import com.cactusknights.chefbook.data.dto.remote.auth.CredentialsBody
import com.cactusknights.chefbook.data.dto.remote.auth.RefreshTokenRequest
import com.cactusknights.chefbook.data.dto.remote.auth.toEntity
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.AuthApi
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.common.Tokens
import java.util.*

class RemoteAuthSource(
    private val api: AuthApi,
    private val handleResponse: INetworkHandler,
) : IAuthSource {

    override suspend fun signUp(email: String, password: String) =
        handleResponse { api.signUp(CredentialsBody(email = email, password = password, userId = UUID.randomUUID().toString())) }
            .toActionStatus().asEmpty()

    override suspend fun signIn(email: String, password: String): ActionStatus<Tokens> {
        val result = handleResponse { api.signIn(CredentialsBody(email = email, password = password)) }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().toEntity())
    }

    override suspend fun signOut(refreshToken: String): SimpleAction =
        handleResponse { api.signOut(RefreshTokenRequest(refreshToken)) }.toActionStatus().asEmpty()

}
