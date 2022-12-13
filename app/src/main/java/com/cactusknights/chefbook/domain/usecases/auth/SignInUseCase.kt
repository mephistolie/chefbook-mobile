package com.cactusknights.chefbook.domain.usecases.auth

import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.settings.Mode
import com.cactusknights.chefbook.domain.interfaces.IAuthRepo
import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import com.cactusknights.chefbook.domain.interfaces.ISettingsRepo
import com.cactusknights.chefbook.domain.usecases.common.IRefreshDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISignInUseCase {
    suspend operator fun invoke(email: String, password: String): Flow<SimpleAction>
}

class SignInUseCase(
    private val authRepo: IAuthRepo,
    private val tokensRepo: ISessionRepo,
    private val settingsRepo: ISettingsRepo,
    private val refreshDataUseCase: IRefreshDataUseCase,
) : ISignInUseCase {
    override suspend operator fun invoke(email: String, password: String): Flow<SimpleAction> = flow {
        emit(Loading)

        val result = authRepo.signIn(email, password)
        if (result.isSuccess()) {
            val tokens = result.data()
            tokensRepo.saveTokens(tokens)
            settingsRepo.setAppMode(Mode.ONLINE)
            refreshDataUseCase()
        }

        emit(result.asEmpty())
    }
}
