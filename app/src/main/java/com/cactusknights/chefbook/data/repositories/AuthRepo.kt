package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.data.IAuthSource
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.common.Tokens
import com.cactusknights.chefbook.domain.interfaces.IAuthRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    @Remote
    private val remote: IAuthSource,
) : IAuthRepo {

    override suspend fun signUp(email: String, password: String): SimpleAction =
        remote.signUp(email, password)

    override suspend fun signIn(email: String, password: String): ActionStatus<Tokens> =
        remote.signIn(email, password)

    override suspend fun signOut(refreshToken: String): SimpleAction =
        remote.signOut(refreshToken)

}
