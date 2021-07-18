package com.cactusknights.chefbook.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.enums.PasswordStates
import com.cactusknights.chefbook.models.Recipe


object Utils {

    fun checkAuthFields(email: String, passwordText: String, repeatPasswordText: String, context: Context): Boolean {
        if (email.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(context, R.string.empty_fields, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!email.contains('@') || !email.contains('.')) {
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show()
            return false
        }
        if (passwordText != repeatPasswordText && repeatPasswordText.isNotEmpty()) {
            Toast.makeText(context, R.string.password_mismatch, Toast.LENGTH_SHORT).show()
            return false
        }
        when (validatePassword(passwordText)) {
            PasswordStates.SPACE -> { Toast.makeText(context, R.string.space_password, Toast.LENGTH_SHORT).show() }
            PasswordStates.SHORT -> { Toast.makeText(context, R.string.short_password, Toast.LENGTH_SHORT).show() }
            PasswordStates.LOWER -> { Toast.makeText(context, R.string.lower_password, Toast.LENGTH_SHORT).show() }
            PasswordStates.UPPER -> { Toast.makeText(context, R.string.upper_password, Toast.LENGTH_SHORT).show() }
            else -> return true
        }
        return false
    }

    private fun validatePassword(password: String): PasswordStates {

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

    fun <T> areListsSame(first: ArrayList<T>?, second: ArrayList<T>?): Boolean {
        if (first.isNullOrEmpty() || second.isNullOrEmpty()) return false
        if (first == second) return true
        if (first.size != second.size) return false
        for (index in 0 until first.size) {
            if (first[index]!! == second[index])
                return false
        }
        return true
    }

    fun shareRecipe(recipe: Recipe, resources: Resources, callback: (intent: Intent) -> Unit) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        var recipeDescription = recipe.name.uppercase() + "\n\n"
        recipeDescription += resources.getString(R.string.servings) + " " + recipe.servings + "\n"
        recipeDescription += resources.getString(R.string.time) + " " + recipe.time + "\n"
        recipeDescription += "\n" + resources.getString(R.string.ingredients) + ":\n"
        for (ingredient in recipe.ingredients) {
            recipeDescription += if (ingredient.isSection) "${ingredient.name.uppercase()}\n" else "• ${ingredient.name}\n"
        }
        recipeDescription += "\n" + resources.getString(R.string.cooking) + "\n"
        for (i in recipe.cooking.indices) {
            recipeDescription += (i+1).toString() + ". " + recipe.cooking[i] + "\n"
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeDescription)
        shareIntent.type = "text/html"
        callback(shareIntent)
    }
}