package com.cactusknights.chefbook.repositories.remote.datasources

import com.cactusknights.chefbook.core.datastore.SessionManager
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.core.retrofit.AuthData
import com.cactusknights.chefbook.core.retrofit.RefreshToken
import com.cactusknights.chefbook.core.retrofit.proto
import com.cactusknights.chefbook.repositories.AuthDataSource
import com.cactusknights.chefbook.repositories.remote.api.AuthApi
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteAuthDataSource @Inject constructor(
    private val api: AuthApi,
    private val session: SessionManager
) : AuthDataSource {

    private var user: MutableStateFlow<User?> = MutableStateFlow(null)

    override suspend fun signUp(email: String, password: String) {
        api.signUp(AuthData(email = email, password = password))
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        val response = api.signIn(AuthData(email = email, password = password))
        if (!response.isSuccessful) throw IOException()
        session.saveTokens(response.body()?.proto())
        return true
    }

    override suspend fun signOut() {
        try { api.signOut(RefreshToken(session.getRefreshToken())) }
        finally { session.clearTokens() }
        user.emit(null)
    }
}