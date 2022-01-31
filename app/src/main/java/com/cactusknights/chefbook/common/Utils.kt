package com.cactusknights.chefbook.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.AsyncListDiffer
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.*


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

    fun sendEmail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:" + context.resources.getString(R.string.support_email))
        context.startActivity(intent)
    }

    fun minutesToTimeString(minutes: Int, resources: Resources): String {
        var str = ""
        if (minutes % 60 != 0) str = str + (minutes % 60).toString() + " " + resources.getString(R.string.minutes)
        if (minutes >= 60) {
            str = (minutes / 60).toString() + " " + resources.getString(R.string.hours) + " "+ str
        }
        return str
    }

    fun <T> AsyncListDiffer<T>.forceSubmitList(list: List<T>) {
        this.submitList(list.toList())
    }
}