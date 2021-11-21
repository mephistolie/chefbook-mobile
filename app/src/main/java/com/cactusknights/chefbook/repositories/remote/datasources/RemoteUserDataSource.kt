package com.cactusknights.chefbook.repositories.remote.datasources

import com.cactusknights.chefbook.domain.UserDataSource
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.toUser
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(
    private val api: ChefBookApi
) : UserDataSource {

    override suspend fun getUserInfo(): User {
        val response = api.getUserInfo()
        return response.body()!!.toUser()
    }
}