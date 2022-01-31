package com.cactusknights.chefbook.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type

enum class CookingStepTypes {
    STEP, SECTION
}

data class CookingStep(
    var text: String = "",
    var link: String? = null,
    var time: Int? = null,
    var pictures: ArrayList<String> = arrayListOf(),
    val type: CookingStepTypes = CookingStepTypes.STEP
) : Serializable

fun ArrayList<CookingStep>.toJson(): String {
    return Gson().toJson(this)
}


fun String.toCooking(): java.util.ArrayList<CookingStep> {
    val type: Type = object : TypeToken<java.util.ArrayList<CookingStep>>() {}.type
    return Gson().fromJson(this, type)
}