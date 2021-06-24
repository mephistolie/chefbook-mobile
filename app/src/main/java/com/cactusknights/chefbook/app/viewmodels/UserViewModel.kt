package com.cactusknights.chefbook.app.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cactusknights.chefbook.app.models.FirebaseRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(app: Application): ViewModel() {

    private var firebaseRepository = FirebaseRepository(app)
    private var user = firebaseRepository.getUser()

    fun signup(email: String?, password: String?, repeatPassword: String?) {
        firebaseRepository.signup(email, password, repeatPassword)
    }

    fun logonEmail(email: String?, password: String?) {
        firebaseRepository.logonEmail(email, password)
    }

    fun restorePassword(email: String?) {
        firebaseRepository.restorePassword(email)
    }

    fun logonGoogle(idToken: String) {
        firebaseRepository.logonGoogle(idToken)
    }

    fun getUser(): MutableLiveData<FirebaseUser> {
        return user
    }

    fun logout() {
        firebaseRepository.logout()
    }

    fun isLoggedIn(): Boolean {
        return firebaseRepository.isLoggedIn()
    }
}