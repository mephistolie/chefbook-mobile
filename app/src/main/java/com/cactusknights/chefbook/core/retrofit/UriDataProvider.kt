package com.cactusknights.chefbook.core.retrofit

import android.webkit.URLUtil
import com.cactusknights.chefbook.common.retrofit.CommonApi
import java.io.File
import java.io.IOException
import javax.inject.Inject

enum class UriType {
    PATH, LINK
}

interface UriDataProvider {
    suspend fun getData(uriString: String) : ByteArray
    suspend fun getUriType(uriString: String) : UriType
}

class UriDataProviderImpl @Inject constructor(private val api: CommonApi) : UriDataProvider {

    override suspend fun getData(uriString: String) : ByteArray {
        return if (URLUtil.isValidUrl(uriString)) {
            val response = api.getFile(uriString)
            if (response.code() != 200 || response.body() == null) throw IOException()
            response.body()!!.byteStream().readBytes()
        } else {
            File(uriString).readBytes()
        }
    }

    override suspend fun getUriType(uriString: String) : UriType {
        return if (URLUtil.isValidUrl(uriString)) { UriType.LINK }
        else { UriType.PATH }
    }
}