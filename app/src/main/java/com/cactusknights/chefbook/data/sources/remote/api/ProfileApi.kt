package com.cactusknights.chefbook.data.sources.remote.api

import com.cactusknights.chefbook.core.retrofit.LinkResponse
import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.data.sources.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("/v1/profile")
    suspend fun getUserInfo(): Response<UserDto>

    @PUT("/v1/profile/change-name")
    suspend fun changeName(@Body username: UsernameInputDto): Response<MessageResponse>

    @Multipart
    @POST("/v1/profile/avatar")
    suspend fun uploadAvatar(@Part image: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/profile/avatar")
    suspend fun deleteAvatar(): Response<MessageResponse>

    @GET("/v1/profile/key")
    suspend fun getUserKeyLink() : Response<LinkResponse>

    @Multipart
    @POST("/v1/profile/key")
    suspend fun uploadUserKey(@Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/profile/key")
    suspend fun deleteUserKey(): Response<MessageResponse>
}