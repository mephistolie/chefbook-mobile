package com.cactusknights.chefbook.repositories


import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.cactusknights.chefbook.helpers.Dialogs
import com.cactusknights.chefbook.interfaces.AuthProvider
import com.cactusknights.chefbook.migration.DataBaseHandler
import com.cactusknights.chefbook.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class FirebaseAuthRepository: AuthProvider {

    private var auth = FirebaseAuth.getInstance()
    private var user: MutableLiveData<User> = MutableLiveData(getChefBookUser(auth.currentUser))

    override fun signupEmail(email: String, password: String, callback: (isSignedUp: Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                registerUser(auth.currentUser, callback)
            }.addOnFailureListener { callback(false) }
    }

    override fun logonEmail(email: String, password: String, callback: (isLoggedIn: Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                registerUser(auth.currentUser, callback)
            }.addOnFailureListener { callback(false) }
    }

    override fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    override fun logonGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                registerUser(auth.currentUser, callback) }
            .addOnFailureListener { callback(false) }
    }

    override fun getCurrentUser(): MutableLiveData<User> { return user }

    override fun isPremium(): Boolean {
        val currentUser = user.value
        return currentUser?.isPremium ?: false
    }
    
    override fun logout() {
        if (auth.currentUser != null) {
            auth.signOut()
            user.postValue(null)
        }
    }

    private fun registerUser(currentUser: FirebaseUser?, callback: (isLoggedIn: Boolean) -> Unit) {
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
                    firestore.collection("users").document(currentUser.uid)
                        .update(updatedInfo).addOnSuccessListener {
                            user.postValue(getChefBookUser(auth.currentUser))
                            callback(true)
                        }
                else {
                    user.postValue(getChefBookUser(auth.currentUser))
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
                    freeUser?.isPremium = false
                    user.postValue(freeUser)
                }
            }
            User(
                firebaseUser.uid,
                if (firebaseUser.displayName != null) firebaseUser.displayName!! else "",
                if (firebaseUser.email != null) firebaseUser.email!! else "",
                if (firebaseUser.phoneNumber != null) firebaseUser.phoneNumber!! else "")
        } else null
    }

    override fun buyPremium(donation_type: String, activity: Activity) {
        val currentUser = user.value
        if (currentUser != null) {
            val purchasesUpdatedListener =
                PurchasesUpdatedListener { billingResult, purchases ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("users").document(currentUser.uid).update(mapOf(
                            "isPremium" to true
                        ))
                        Dialogs.openGratitudeDialog(activity)
                    }
                }

            val billingClient = BillingClient.newBuilder(activity)
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
    }

    companion object {

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