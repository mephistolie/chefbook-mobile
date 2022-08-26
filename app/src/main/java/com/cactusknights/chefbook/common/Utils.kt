package com.cactusknights.chefbook.common

import android.app.Activity
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.cactusknights.chefbook.R

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

    fun minutesToTimeString(minutes: Int, resources: Resources): String {
        var str = ""
        if (minutes % 60 != 0) str = str + (minutes % 60).toString() + " " + resources.getString(R.string.common_general_minutes)
        if (minutes >= 60) {
            str = (minutes / 60).toString() + " " + resources.getString(R.string.common_general_hours) + " "+ str
        }
        return str
    }
}
