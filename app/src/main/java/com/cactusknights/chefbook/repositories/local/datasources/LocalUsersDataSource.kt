package com.cactusknights.chefbook.repositories.local.datasources

import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.UserDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUsersDataSource @Inject constructor(): UserDataSource {

    override suspend fun getUserInfo(): User {
        return User(
            name = "Local User",
            premium = null,
            isLocal = true
        )
    }
}