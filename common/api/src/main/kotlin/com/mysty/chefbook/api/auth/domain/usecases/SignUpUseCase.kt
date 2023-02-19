package com.mysty.chefbook.api.auth.domain.usecases

import com.mysty.chefbook.api.auth.domain.IAuthRepo
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.SimpleAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISignUpUseCase {
    suspend operator fun invoke(email: String, password: String): Flow<SimpleAction>
}

internal class SignUpUseCase(
    private val authRepo: IAuthRepo,
) : ISignUpUseCase {
    override suspend operator fun invoke(email: String, password: String): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(authRepo.signUp(email, password))
    }
}
