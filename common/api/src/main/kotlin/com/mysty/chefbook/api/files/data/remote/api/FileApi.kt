package com.mysty.chefbook.api.files.data.remote.api

import com.mysty.chefbook.api.common.network.dto.RequestResult
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

internal interface FileApi {

    @GET
    suspend fun getFile(@Url link: String) : RequestResult<ResponseBody>

}
