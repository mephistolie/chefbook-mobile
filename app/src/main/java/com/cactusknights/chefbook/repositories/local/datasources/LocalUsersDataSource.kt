package com.cactusknights.chefbook.repositories.local.datasources

import android.content.SharedPreferences
import com.cactusknights.chefbook.base.Constants
import com.cactusknights.chefbook.domain.UserDataSource
import com.cactusknights.chefbook.models.User
import javax.inject.Inject

class LocalUsersDataSource : UserDataSource {

    override suspend fun getUserInfo(): User {
        return User(
            name = "Local User",
            premium = null,
            isLocal = true
        )
    }
}