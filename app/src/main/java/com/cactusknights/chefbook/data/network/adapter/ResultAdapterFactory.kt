package com.cactusknights.chefbook.data.network.adapter

import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

class ResultAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawReturnType: Class<*> = getRawType(returnType)
        if (rawReturnType == Call::class.java) {
            if (returnType is ParameterizedType) {
                val callInnerType: Type = getParameterUpperBound(0, returnType)
                if (getRawType(callInnerType) == RequestResult::class.java) {
                    if (callInnerType is ParameterizedType) {
                        val resultInnerType = getParameterUpperBound(0, callInnerType)
                        return ResultCallAdapter<Any?>(resultInnerType)
                    }
                    return ResultCallAdapter<Nothing>(Nothing::class.java)
                }
            }
        }

        return null
    }
}
