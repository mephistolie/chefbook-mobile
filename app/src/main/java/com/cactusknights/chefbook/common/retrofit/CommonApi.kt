package com.cactusknights.chefbook.common.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CommonApi {

    @GET
    suspend fun getFile(@Url link: String) : Response<ResponseBody>
}