package com.mysty.chefbook.core.ui.utils

import android.content.res.Resources
import com.mysty.chefbook.core.ui.R

object TimeUtils {
    fun minutesToTimeString(minutes: Int, resources: Resources): String {
        var str = ""
        if (minutes % 60 != 0) str = str + (minutes % 60).toString() + " " + resources.getString(R.string.common_general_minutes)
        if (minutes >= 60) {
            str = (minutes / 60).toString() + " " + resources.getString(R.string.common_general_hours) + " "+ str
        }
        return str
    }
}
