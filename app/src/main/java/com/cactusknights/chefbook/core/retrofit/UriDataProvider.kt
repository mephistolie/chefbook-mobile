package com.cactusknights.chefbook.core.retrofit

import android.webkit.URLUtil
import com.cactusknights.chefbook.common.retrofit.CommonApi
import java.io.File
import java.io.IOException
import javax.inject.Inject

interface UriDataProvider {
    suspend fun getData(uri: String) : ByteArray
}

class UriDataProviderImpl @Inject constructor(private val api: CommonApi) : UriDataProvider {

    override suspend fun getData(uri: String) : ByteArray {
        if (URLUtil.isValidUrl(uri)) {
            val response = api.getFile(uri)
            if (response.code() != 200 || response.body() == null) throw IOException()
            return response.body()!!.byteStream().readBytes()
        } else {
            return File(uri).readBytes()
        }
    }
}