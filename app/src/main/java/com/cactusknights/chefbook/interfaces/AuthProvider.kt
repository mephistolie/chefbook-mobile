package com.cactusknights.chefbook.interfaces

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.cactusknights.chefbook.models.User

interface AuthProvider {

    fun signupEmail(email: String, password: String, callback: (isSignedUp: Boolean) -> Unit)
    fun logonEmail(email: String, password: String, callback: (isLoggedIn: Boolean) -> Unit)
    fun logonGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit)
    fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit)
    fun isPremium(): Boolean
    fun buyPremium(donation_type: String, activity: Activity)
    fun getCurrentUser(): MutableLiveData<User>
    fun logout()

}