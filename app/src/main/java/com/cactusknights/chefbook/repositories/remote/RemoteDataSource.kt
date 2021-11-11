package com.cactusknights.chefbook.repositories.remote

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.cactusknights.chefbook.common.AuthException
import com.cactusknights.chefbook.common.Constants.ACCESS_TOKEN
import com.cactusknights.chefbook.common.Constants.REFRESH_TOKEN
import com.cactusknights.chefbook.legacy.dialogs.GratitudeDialog
import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.models.retrofit.AuthData
import com.cactusknights.chefbook.models.retrofit.RefreshToken
import com.cactusknights.chefbook.models.retrofit.TokenResponse
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ChefBookApi,
    private val preferences: SharedPreferences
) : AuthProvider {

    private var auth = FirebaseAuth.getInstance()
    private var user: MutableStateFlow<User?> = MutableStateFlow(null)

    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    private lateinit var billingClient: BillingClient

    override suspend fun listenAuthState(): MutableStateFlow<User?> {
        user.value = null
        return user
    }

    override suspend fun isLoggedIn(): Boolean {
        return user.value != null
    }

    override suspend fun isPremium(): Boolean {
        return false
    }

    override suspend fun signUp(email: String, password: String) {
        api.signUp(AuthData(email = email, password = password))
    }

    override suspend fun signInEmail(email: String, password: String) {
        val response = api.signIn(
            AuthData(
                email = email,
                password = password
            )
        )
        processTokenResponse(response)
    }

    private fun processTokenResponse(response: Response<TokenResponse>) {
        val tokens = response.body()
        if (response.isSuccessful && tokens != null) {
            preferences.edit()
                .putString(ACCESS_TOKEN, tokens.accessToken)
                .putString(REFRESH_TOKEN, tokens.refreshToken)
                .apply()
        } else throw IOException()
    }

    suspend fun refreshSession() {
        val refreshToken = preferences.getString(REFRESH_TOKEN, "").orEmpty()
        val response = api.refreshSession(RefreshToken(refreshToken))
        processTokenResponse(response)
    }

    suspend fun getUserInfo(): User {
        val response = api.getUserInfo()
        handleAuthError(response.code())
        return response.body()!!.toUser()
    }

    override suspend fun signInGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                updateUserData(auth.currentUser, callback)
            }
            .addOnFailureListener { callback(false) }
    }

    override suspend fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit) {
    }

    override suspend fun logout() {
    }

    override suspend fun buyPremium(donation_type: String, activity: AppCompatActivity) {
    }

    private fun updateUserData(
        currentUser: FirebaseUser?,
        callback: (isLoggedIn: Boolean) -> Unit
    ) {
    }

    companion object {
        private fun handleAuthError(responseCode : Int) {
            if (responseCode == 401) {
                throw AuthException()
            }
        }
    }
}