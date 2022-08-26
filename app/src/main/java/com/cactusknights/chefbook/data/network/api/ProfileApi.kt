package com.cactusknights.chefbook.data.network.api

import com.cactusknights.chefbook.data.dto.remote.common.LinkResponse
import com.cactusknights.chefbook.data.dto.remote.common.MessageResponse
import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import com.cactusknights.chefbook.data.dto.remote.profile.ChangePasswordRequest
import com.cactusknights.chefbook.data.dto.remote.profile.ChangeUsernameRequest
import com.cactusknights.chefbook.data.dto.remote.profile.ProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileApi {

    @GET("/v1/profile")
    suspend fun getUserInfo(@Query("user_id") search: String? = null): RequestResult<ProfileResponse>

    @PUT("/v1/profile/username")
    suspend fun changeName(@Body username: ChangeUsernameRequest): RequestResult<MessageResponse>

    @PUT("/v1/profile/password")
    suspend fun changePassword(@Body body: ChangePasswordRequest): RequestResult<MessageResponse>

    @Multipart
    @POST("/v1/profile/avatar")
    suspend fun uploadAvatar(@Part image: MultipartBody.Part): RequestResult<LinkResponse>

    @DELETE("/v1/profile/avatar")
    suspend fun deleteAvatar(): RequestResult<MessageResponse>

    @GET("/v1/profile/key")
    suspend fun getUserKeyLink(): RequestResult<LinkResponse>

    @Multipart
    @POST("/v1/profile/key")
    suspend fun uploadUserKey(@Part key: MultipartBody.Part): RequestResult<LinkResponse>

    @DELETE("/v1/profile/key")
    suspend fun deleteUserKey(): RequestResult<MessageResponse>
}
