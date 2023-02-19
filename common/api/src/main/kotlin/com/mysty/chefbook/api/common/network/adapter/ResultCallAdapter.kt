package com.mysty.chefbook.api.common.network.adapter

import com.mysty.chefbook.api.common.network.dto.RequestResult
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter

internal class ResultCallAdapter<R>(private val type: Type) : CallAdapter<R, Call<RequestResult<R>>> {

    override fun responseType() = type

    override fun adapt(call: Call<R>): Call<RequestResult<R>> = ResultCall(call)
}
