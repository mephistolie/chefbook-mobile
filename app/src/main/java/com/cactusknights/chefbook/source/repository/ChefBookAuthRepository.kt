package com.cactusknights.chefbook.source.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.cactusknights.chefbook.common.Constants.ACCESS_TOKEN
import com.cactusknights.chefbook.common.Constants.REFRESH_TOKEN
import com.cactusknights.chefbook.dialogs.GratitudeDialog
import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.models.retrofit.AuthData
import com.cactusknights.chefbook.models.retrofit.RefreshToken
import com.cactusknights.chefbook.models.retrofit.TokenResponse
import com.cactusknights.chefbook.source.remote.ChefBookApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ChefBookAuthRepository @Inject constructor(
    private val api: ChefBookApi,
    private val preferences: SharedPreferences
) : AuthProvider {

    private var auth = FirebaseAuth.getInstance()
    private var user: MutableStateFlow<User?> = MutableStateFlow(null)

    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    private lateinit var billingClient: BillingClient

    override suspend fun listenToUser(): MutableStateFlow<User?> {
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

    private suspend fun refreshSession() {
        val refreshToken = preferences.getString(REFRESH_TOKEN, "").orEmpty()
        val response = api.refreshSession(RefreshToken(refreshToken))
        processTokenResponse(response)
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
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    override suspend fun logout() {
        if (auth.currentUser != null) {
            auth.signOut()
            user.value = null
        }
    }

    override suspend fun buyPremium(donation_type: String, activity: AppCompatActivity) {
        purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        consumePurchase(purchase.purchaseToken, activity)
                    }
                }
            }

        billingClient = BillingClient.newBuilder(activity)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(p0: BillingResult) {
                if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                    val skuList = ArrayList<String>()
                    skuList.add(donation_type)
                    val params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(
                        BillingClient.SkuType.INAPP
                    )
                    billingClient.querySkuDetailsAsync(params.build()) { _, skuDetailsList ->
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList!![0])
                            .build()
                        billingClient.launchBillingFlow(activity, flowParams).responseCode
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }

    private fun consumePurchase(purchaseToken: String, activity: AppCompatActivity) {
        val currentUser = user.value
        if (currentUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build()
            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    firestore.collection("users").document(currentUser.id).update(
                        mapOf(
                            "isPremium" to true
                        )
                    )
                    GratitudeDialog().show(activity.supportFragmentManager, "Gratitude")
                }
            }
        }
    }

    private fun updateUserData(
        currentUser: FirebaseUser?,
        callback: (isLoggedIn: Boolean) -> Unit
    ) {
        if (currentUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->

                    val updatedInfo = mutableMapOf<String, Any>()
                    val name = document["name"] as String?
                    val email = document["email"] as String?
                    val phone = document["phone"] as String?
                    val isPremium = document["isPremium"] as Boolean?
                    val shoppingList = document["shoppingList"] as ArrayList<*>?

                    if (name.isNullOrEmpty() && !currentUser.displayName.isNullOrEmpty()) updatedInfo["name"] =
                        currentUser.displayName!!
                    if (email.isNullOrEmpty() && !currentUser.email.isNullOrEmpty()) updatedInfo["email"] =
                        currentUser.email!!
                    if (phone.isNullOrEmpty() && !currentUser.phoneNumber.isNullOrEmpty()) updatedInfo["phone"] =
                        currentUser.phoneNumber!!
                    if (isPremium == null) updatedInfo["isPremium"] = false
                    if (shoppingList == null) updatedInfo["shoppingList"] = arrayListOf<String>()

                    if (updatedInfo.isNotEmpty())
                        if (document.exists())
                            firestore.collection("users").document(currentUser.uid)
                                .update(updatedInfo).addOnSuccessListener {
                                    user.value = getChefBookUser(auth.currentUser)
                                    callback(true)
                                }
                        else
                            firestore.collection("users").document(currentUser.uid)
                                .set(updatedInfo).addOnSuccessListener {
                                    user.value = getChefBookUser(auth.currentUser)
                                    callback(true)
                                }
                    else {
                        user.value = getChefBookUser(auth.currentUser)
                        callback(true)
                    }
                }
        }
    }

    private fun getChefBookUser(firebaseUser: FirebaseUser?): User? {
        return if (firebaseUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").document(firebaseUser.uid).get().addOnSuccessListener {
                val isPremium = it["isPremium"] as Boolean?
                if (isPremium == null || isPremium == false) {
                    val freeUser = user.value
//                    freeUser?.isPremium = false
                    user.value = freeUser
                }
            }
            User(
                firebaseUser.uid,
                if (firebaseUser.displayName != null) firebaseUser.displayName!! else "",
                if (firebaseUser.email != null) firebaseUser.email!! else "",
//                if (firebaseUser.phoneNumber != null) firebaseUser.phoneNumber!! else "")
            )
        } else null
    }
}