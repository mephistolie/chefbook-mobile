package com.cactusknights.chefbook.domain.usecases.auth

import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IAuthRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISignUpUseCase {
    suspend operator fun invoke(email: String, password: String): Flow<SimpleAction>
}

class SignUpUseCase(
    private val authRepo: IAuthRepo,
) : ISignUpUseCase {
    override suspend operator fun invoke(email: String, password: String): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(authRepo.signUp(email, password))
    }
}
