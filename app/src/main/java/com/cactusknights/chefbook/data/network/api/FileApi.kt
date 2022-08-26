package com.cactusknights.chefbook.data.network.api

import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface FileApi {

    @GET
    suspend fun getFile(@Url link: String) : RequestResult<ResponseBody>

}
