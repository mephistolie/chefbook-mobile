package com.cactusknights.chefbook.interfaces

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.cactusknights.chefbook.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthProvider {

    suspend fun listenTotUser(): MutableStateFlow<User?>
    suspend fun isLoggedIn(): Boolean
    suspend fun isPremium(): Boolean

    suspend fun signupEmail(email: String, password: String, callback: (isSignedUp: Boolean) -> Unit)
    suspend fun logonEmail(email: String, password: String, callback: (isLoggedIn: Boolean) -> Unit)
    suspend fun logonGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit)
    suspend fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit)
    suspend fun logout()
    suspend fun buyPremium(donation_type: String, activity: AppCompatActivity)

}