package com.cactusknights.chefbook.domain.usecases.auth

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.interfaces.IAuthRepo
import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IChooseLocalModeUseCase {
    suspend operator fun invoke(): Flow<SimpleAction>
}

class ChooseLocalModeUseCase @Inject constructor(
    private val authRepo: IAuthRepo,
    private val tokensRepo: ISessionRepo
): IChooseLocalModeUseCase {
    override suspend operator fun invoke(): Flow<SimpleAction> = flow {
        emit(ActionStatus.Loading)

        tokensRepo.getRefreshToken()?.let { refreshToken -> authRepo.signOut(refreshToken) }
        tokensRepo.clearTokens()

        emit(SuccessResult)
    }
}
