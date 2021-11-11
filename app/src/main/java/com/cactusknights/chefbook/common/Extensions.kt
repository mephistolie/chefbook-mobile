package com.cactusknights.chefbook.legacy.helpers

import android.content.Context
import android.widget.Toast

fun <T : Context> T.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun <T : Context> T.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}