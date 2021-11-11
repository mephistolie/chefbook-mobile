package com.cactusknights.chefbook.legacy.repositories

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.cactusknights.chefbook.legacy.migration.DataBaseHandler
import com.cactusknights.chefbook.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class FirebaseAuthRepository {

    private var auth = FirebaseAuth.getInstance()
    private var user: MutableStateFlow<User?> = MutableStateFlow(getChefBookUser(auth.currentUser))

    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    private lateinit var billingClient: BillingClient

    suspend fun listenToUser(): MutableStateFlow<User?> { return user }

    suspend fun isLoggedIn(): Boolean { return user.value != null }

    suspend fun signUp(email: String, password: String, callback: (isSignedUp: Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserData(auth.currentUser, callback)
            }.addOnFailureListener { callback(false) }
    }

    suspend fun signInEmail(email: String, password: String, callback: (isLoggedIn: Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserData(auth.currentUser, callback)
            }.addOnFailureListener { callback(false) }
    }

     suspend fun signInGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                updateUserData(auth.currentUser, callback) }
            .addOnFailureListener { callback(false) }
    }

     suspend fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

     suspend fun logout() {
        if (auth.currentUser != null) {
            auth.signOut()
            user.value = null
        }
    }

     suspend fun buyPremium(donation_type: String, activity: AppCompatActivity) {
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
                        BillingClient.SkuType.INAPP)
                    billingClient.querySkuDetailsAsync(params.build()) { _, skuDetailsList ->
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList!![0])
                            .build()
                        billingClient.launchBillingFlow(activity, flowParams).responseCode
                    }
                }
            }
            override fun onBillingServiceDisconnected() { }
        })
    }

    private fun consumePurchase(purchaseToken: String, activity: AppCompatActivity) {
        val currentUser = user.value
        if (currentUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build()
            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                }
            }
        }
    }

    private fun updateUserData(currentUser: FirebaseUser?, callback: (isLoggedIn: Boolean) -> Unit) {
        if (currentUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").document(currentUser.uid).get().addOnSuccessListener { document ->

                val updatedInfo = mutableMapOf<String, Any>()
                val name = document["name"] as String?
                val email = document["email"] as String?
                val phone = document["phone"] as String?
                val isPremium = document["isPremium"] as Boolean?
                val shoppingList = document["shoppingList"] as ArrayList<*>?

                if (name.isNullOrEmpty() && !currentUser.displayName.isNullOrEmpty()) updatedInfo["name"] = currentUser.displayName!!
                if (email.isNullOrEmpty() && !currentUser.email.isNullOrEmpty()) updatedInfo["email"] = currentUser.email!!
                if (phone.isNullOrEmpty() && !currentUser.phoneNumber.isNullOrEmpty()) updatedInfo["phone"] = currentUser.phoneNumber!!
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
                    user.value = freeUser
                }
            }
            User(
                firebaseUser.uid.toInt(),
                if (firebaseUser.displayName != null) firebaseUser.displayName!! else "",
                if (firebaseUser.email != null) firebaseUser.email!! else "")
        } else null
    }

    companion object {

        val instance = FirebaseAuthRepository()

        private var auth = FirebaseAuth.getInstance()

        fun migrateToFirebase(legacyDatabase: File, context: Context) {
            val user = auth.currentUser
            if (user != null) {
                val database = DataBaseHandler(context)
                val recipes = database.getData()
                val firestore = FirebaseFirestore.getInstance()
                val batch = firestore.batch()
                recipes.forEach {
                    val doc = firestore.collection("users").document(user.uid).collection("recipes").document()
                    batch.set(doc, it)
                }
                batch.commit().addOnSuccessListener {
                    legacyDatabase.delete()
                }
            }
        }
    }
}