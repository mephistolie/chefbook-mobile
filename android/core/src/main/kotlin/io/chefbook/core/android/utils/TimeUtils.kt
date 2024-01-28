package io.chefbook.core.android.utils

import android.content.res.Resources
import io.chefbook.core.android.R

fun minutesToTimeString(minutes: Int?, resources: Resources): String {
    var str = ""
    if (minutes != null) {
        if (minutes % 60 != 0) str =
            str + (minutes % 60).toString() + " " + resources.getString(R.string.common_general_minutes)
        if (minutes >= 60) {
            str =
                (minutes / 60).toString() + " " + resources.getString(R.string.common_general_hours) + " " + str
        }
    }
    return str
}
