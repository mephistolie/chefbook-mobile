package com.mysty.chefbook.api.auth.domain.usecases

import com.mysty.chefbook.api.auth.domain.IAuthRepo
import com.mysty.chefbook.api.auth.domain.ITokensRepo
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

private const val REFRESH_SESSION_TIMEOUT = 100L

interface IRefreshSessionUseCase {
    suspend operator fun invoke(): SimpleAction
}

internal class RefreshSessionUseCase(
    private val authRepo: IAuthRepo,
    private val tokensRepo: ITokensRepo,
) : IRefreshSessionUseCase {

    override suspend operator fun invoke(): SimpleAction =
        if (!mutex.isLocked) {
            mutex.withLock {
                Timber.i("Refreshing session...")
                tokensRepo.getRefreshToken()?.let { refreshToken ->
                    val result = authRepo.refreshTokens(refreshToken)

                    if (result.isSuccess()) {
                        Timber.i("Session refreshed")
                        tokensRepo.saveTokens(result.data())
                        SuccessResult
                    } else {
                        Timber.i("Session refresh failed. Signing out...")
                        tokensRepo.clearTokens()
                        SuccessResult
                    }
                }
            }
            SuccessResult
        } else {
            while (mutex.isLocked) delay(REFRESH_SESSION_TIMEOUT)
            SuccessResult
        }

    companion object {
        private val mutex = Mutex()
    }

}
