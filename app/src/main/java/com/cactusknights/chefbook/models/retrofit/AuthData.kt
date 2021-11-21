package com.cactusknights.chefbook.models.retrofit

import com.google.gson.annotations.SerializedName

data class AuthData (
    var email: String = "",
    var password: String = ""
)

data class IdResponse (
    var id: Int = -1,
    var message: String = ""
)

data class MessageResponse (
    var id: String = "",
    var message: String = ""
)

data class TokenResponse (
    @SerializedName("access_token") var accessToken: String = "",
    @SerializedName("refresh_token") var refreshToken: String = ""
)

data class RefreshToken (
    @SerializedName("refresh_token") var refreshToken: String = ""
)