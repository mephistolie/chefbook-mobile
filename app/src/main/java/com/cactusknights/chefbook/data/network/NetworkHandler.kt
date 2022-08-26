package com.cactusknights.chefbook.data.network

import com.cactusknights.chefbook.data.dto.remote.auth.RefreshTokenRequest
import com.cactusknights.chefbook.data.dto.remote.auth.toEntity
import com.cactusknights.chefbook.data.dto.remote.common.ErrorResponseBody
import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import com.cactusknights.chefbook.data.dto.remote.common.toServerError
import com.cactusknights.chefbook.data.network.api.AuthApi
import com.cactusknights.chefbook.domain.entities.action.ServerErrorType
import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import com.cactusknights.chefbook.domain.interfaces.ISourceRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface INetworkHandler {
    suspend operator fun <T> invoke(block: suspend () -> RequestResult<T>): RequestResult<T>
}

@Singleton
class NetworkHandler @Inject constructor(
    private val api: AuthApi,
    private val sourceRepo: ISourceRepo,
    private val tokensRepo: ISessionRepo,
) : INetworkHandler {

    private val mutex: Mutex = Mutex()

    override suspend operator fun <T> invoke(block: suspend () -> RequestResult<T>): RequestResult<T> {
        val initialResult = block()
        return processResult(block, initialResult)
    }

    private suspend fun <T> processResult(block: suspend () -> RequestResult<T>, result: RequestResult<T>): RequestResult<T> {
        val isResultRelevant = when (result) {
            is RequestResult.SuccessResponse -> true
            is RequestResult.ErrorResponse -> acceptErrorResponse(result.body)
            is RequestResult.NetworkError -> acceptNetworkError()
        }

        return if (isResultRelevant) result else block()
    }

    private suspend fun acceptErrorResponse(body: ErrorResponseBody): Boolean {
        val error = body.toServerError()
        if (error.type == ServerErrorType.UNAUTHORIZED) {
            return !refreshSession()
        }

        return true
    }

    private suspend fun acceptNetworkError() : Boolean {
        sourceRepo.setServerAccess(false)
        return true
    }

    private suspend fun refreshSession(): Boolean =
        if (!mutex.isLocked) {
            mutex.withLock {
                tokensRepo.getRefreshToken()?.let { refreshToken ->
                    val result = api.refreshSession(RefreshTokenRequest(refreshToken))
                    if (result is RequestResult.SuccessResponse) {
                        tokensRepo.saveTokens(result.body.toEntity())
                        return true
                    } else if (result is RequestResult.ErrorResponse && result.body.toServerError().type == ServerErrorType.INVALID_REFRESH_TOKEN) {
                        tokensRepo.clearTokens()
                    }
                }
            }
            false
        } else {
            while (mutex.isLocked) {
                delay(200)
            }
            true
        }

}
