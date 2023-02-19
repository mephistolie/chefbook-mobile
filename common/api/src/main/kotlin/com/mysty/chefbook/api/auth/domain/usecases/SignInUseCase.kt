package com.mysty.chefbook.api.auth.domain.usecases

import com.mysty.chefbook.api.auth.domain.IAuthRepo
import com.mysty.chefbook.api.auth.domain.ITokensRepo
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.files.domain.usecases.IRefreshDataUseCase
import com.mysty.chefbook.api.settings.domain.ISettingsRepo
import com.mysty.chefbook.api.settings.domain.entities.Mode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISignInUseCase {
    suspend operator fun invoke(email: String, password: String): Flow<SimpleAction>
}

internal class SignInUseCase(
    private val authRepo: IAuthRepo,
    private val tokensRepo: ITokensRepo,
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
