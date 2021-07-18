package com.cactusknights.chefbook.models

data class User constructor(

    var uid: String = "",
    var displayName: String = "",
    var email: String = "",
    var phone: String = "",
    var isPremium: Boolean = true
    )