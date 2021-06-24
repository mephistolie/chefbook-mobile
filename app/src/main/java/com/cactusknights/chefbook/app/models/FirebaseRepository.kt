package com.cactusknights.chefbook.app.models

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.app.enums.PasswordStates
import com.cactusknights.chefbook.app.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FirebaseRepository(var app: Application) {

    private var auth = FirebaseAuth.getInstance()
    private var user = MutableLiveData<FirebaseUser>()

    fun signup(email: String?, password: String?, repeatPassword: String?) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty() && !repeatPassword.isNullOrEmpty()) {
            if (email.contains('@') && email.contains('.')) {
                if (Utils.validatePassword(password.toString()) == PasswordStates.VALID) {
                    if (password.toString() == repeatPassword.toString()) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(ContextCompat.getMainExecutor(app)) { task ->
                                if (task.isSuccessful) {
                                    registerUser(auth.currentUser)
                                    user.postValue(auth.currentUser)
                                } else {
                                    Toast.makeText(app, R.string.signup_failed, Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(app, R.string.password_mismatch, Toast.LENGTH_SHORT).show()}
                } else {
                    when (Utils.validatePassword(password)) {
                        PasswordStates.SPACE -> {
                            Toast.makeText(app, R.string.space_password, Toast.LENGTH_SHORT).show()
                        }
                        PasswordStates.SHORT -> {
                            Toast.makeText(app, R.string.short_password, Toast.LENGTH_SHORT).show()
                        }
                        PasswordStates.LOWER -> {
                            Toast.makeText(app, R.string.lower_password, Toast.LENGTH_SHORT).show()
                        }
                        PasswordStates.UPPER -> {
                            Toast.makeText(app, R.string.upper_password, Toast.LENGTH_SHORT).show()
                        }
                        else -> Toast.makeText(app, R.string.number_password, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(app, R.string.invalid_email, Toast.LENGTH_SHORT).show()
            }
        } else {Toast.makeText(app, R.string.empty_fields, Toast.LENGTH_SHORT).show()}
    }

    fun logonEmail(email: String?, password: String?) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(ContextCompat.getMainExecutor(app)) { task ->
                    if (task.isSuccessful) {
                        registerUser(auth.currentUser)
                        user.postValue(auth.currentUser)
                    } else {
                        Toast.makeText(app, R.string.authentication_failed, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(app, R.string.empty_fields, Toast.LENGTH_SHORT).show()
        }
    }

    fun restorePassword(email: String?) {
        if (!email.isNullOrEmpty() && email.contains('@') && email.contains('.')) {
            auth.sendPasswordResetEmail(email.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(app, R.string.email_to_reset, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(app, R.string.failed_to_reset, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(app, R.string.invalid_email, Toast.LENGTH_SHORT).show()
        }
    }

    fun logonGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(ContextCompat.getMainExecutor(app)) { task ->
                if (task.isSuccessful) {
                    registerUser(auth.currentUser)
                    user.postValue(auth.currentUser)
                } else {
                    Toast.makeText(app, R.string.failed_to_google, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun getUser(): MutableLiveData<FirebaseUser> {
        return user
    }

    private fun registerUser(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").document(currentUser.uid)
                .set(mapOf(
                    "name" to currentUser.displayName,
                    "email" to currentUser.email
                ))
        }
    }

    fun logout() {
        if (auth.currentUser != null) {
            auth.signOut()
        }
    }

    fun isLoggedIn(): Boolean {return auth.currentUser != null}
}