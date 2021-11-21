package com.cactusknights.chefbook.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.MarkdownTypes


object Utils {

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

    fun shareRecipe(recipe: DecryptedRecipe, resources: Resources, callback: (intent: Intent) -> Unit) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        var recipeDescription = recipe.name.uppercase() + "\n\n"
        recipeDescription += resources.getString(R.string.servings) + " " + recipe.servings + "\n"
        recipeDescription += resources.getString(R.string.time) + " " + recipe.time + "\n"
        recipeDescription += "\n" + resources.getString(R.string.ingredients) + ":\n"
        for (ingredient in recipe.ingredients) {
            recipeDescription += if (ingredient.type == MarkdownTypes.HEADER) "${ingredient.text!!.uppercase()}\n" else "• ${ingredient.text}\n"
        }
        recipeDescription += "\n" + resources.getString(R.string.cooking) + "\n"
        for (i in recipe.cooking.indices) {
            recipeDescription += (i+1).toString() + ". " + recipe.cooking[i] + "\n"
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeDescription)
        shareIntent.type = "text/html"
        callback(shareIntent)
    }

    fun sendEmail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:" + context.resources.getString(R.string.support_email))
        context.startActivity(intent)
    }

    fun getFormattedTimeByMinutes(minutes: Int, resources: Resources): String {
        var str = (minutes % 60).toString() + " " + resources.getString(R.string.minutes)
        if (minutes >= 60) {
            str = (minutes / 60).toString() + " " + resources.getString(R.string.hours) + " "+ str
        }
        return str
    }
}