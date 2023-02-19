package com.mysty.chefbook.api.auth.domain.usecases

import com.mysty.chefbook.api.auth.domain.IAuthRepo
import com.mysty.chefbook.api.auth.domain.ITokensRepo
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IChooseLocalModeUseCase {
    suspend operator fun invoke(): Flow<SimpleAction>
}

internal class ChooseLocalModeUseCase(
    private val authRepo: IAuthRepo,
    private val tokensRepo: ITokensRepo
): IChooseLocalModeUseCase {
    override suspend operator fun invoke(): Flow<SimpleAction> = flow {
        emit(ActionStatus.Loading)

        tokensRepo.getRefreshToken()?.let { refreshToken -> authRepo.signOut(refreshToken) }
        tokensRepo.clearTokens()

        emit(SuccessResult)
    }
}
