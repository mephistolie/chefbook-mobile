package com.cactusknights.chefbook.app.helpers

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cactusknights.chefbook.app.enums.PasswordStates


object Utils {
    fun validatePassword(password: String): PasswordStates {

        val numberValidator = """^(?=.*[0-9]).{8,}$""".toRegex()
        val lowerValidator = """^(?=.*[a-z]).{8,}$""".toRegex()
        val upperValidator = """^(?=.*[A-Z]).{8,}$""".toRegex()
        val spaceValidator = """^(?=\S+$).+""".toRegex()

        if (password.isNotEmpty() && !password.matches(spaceValidator)) return PasswordStates.SPACE
        if (password.length < 8) return PasswordStates.SHORT
        if (!password.matches(lowerValidator)) return PasswordStates.LOWER
        if (!password.matches(upperValidator)) return PasswordStates.UPPER
        if (!password.matches(numberValidator)) return PasswordStates.NUMBER

        return PasswordStates.VALID
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(aClass: Class<T>):T {
            @Suppress("UNCHECKED_CAST")
            return f() as T
        }
    }
}