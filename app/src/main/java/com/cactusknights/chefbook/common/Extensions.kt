package com.cactusknights.chefbook.helpers

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

fun <T : Context> T.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun <T : Context> T.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}