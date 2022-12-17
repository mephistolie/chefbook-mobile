package com.mysty.chefbook.core.ui.utils

import android.content.res.Resources
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.core.utils.TimeUtils

fun TimeUtils.minutesToTimeString(minutes: Int?, resources: Resources): String {
    var str = Strings.EMPTY
    if (minutes != null) {
        if (minutes % 60 != 0) str =
            str + (minutes % 60).toString() + " " + resources.getString(com.mysty.chefbook.core.ui.R.string.common_general_minutes)
        if (minutes >= 60) {
            str =
                (minutes / 60).toString() + " " + resources.getString(com.mysty.chefbook.core.ui.R.string.common_general_hours) + " " + str
        }
    }
    return str
}
