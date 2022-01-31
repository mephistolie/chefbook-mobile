package com.cactusknights.chefbook.repositories.remote.api

import com.cactusknights.chefbook.core.retrofit.LinkResponse
import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.repositories.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UsersApi {

    @GET("/v1/users")
    suspend fun getUserInfo(): Response<UserDto>

    @PUT("/v1/users/change-name")
    suspend fun changeName(@Body username: UsernameInputDto): Response<MessageResponse>

    @Multipart
    @POST("/v1/users/avatar")
    suspend fun uploadAvatar(@Part image: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/users/avatar")
    suspend fun deleteAvatar(): Response<MessageResponse>

    @GET("/v1/users/key")
    suspend fun getUserKeyLink() : Response<LinkResponse>

    @Multipart
    @POST("/v1/users/key")
    suspend fun uploadUserKey(@Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/users/key")
    suspend fun deleteUserKey(): Response<MessageResponse>
}