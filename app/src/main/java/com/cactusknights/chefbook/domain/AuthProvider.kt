package com.cactusknights.chefbook.domain

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthProvider {

    suspend fun listenAuthState(): MutableStateFlow<User?>
    suspend fun isLoggedIn(): Boolean
    suspend fun isPremium(): Boolean

    suspend fun signUp(email: String, password: String)
    suspend fun signInEmail(email: String, password: String)
    suspend fun signInGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit)
    suspend fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit)
    suspend fun logout()
    suspend fun buyPremium(donation_type: String, activity: AppCompatActivity)

}