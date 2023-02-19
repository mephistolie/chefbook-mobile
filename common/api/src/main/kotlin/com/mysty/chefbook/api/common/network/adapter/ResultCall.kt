package com.mysty.chefbook.api.common.network.adapter

import com.mysty.chefbook.api.common.network.dto.RequestResult
import com.mysty.chefbook.api.common.network.dto.responses.ErrorResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, RequestResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<RequestResult<T>>) {
        proxy.enqueue(ResultCallback(this, callback))
    }

    override fun cloneImpl(): ResultCall<T> {
        return ResultCall(proxy.clone())
    }

    private class ResultCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<RequestResult<T>>
    ) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result: RequestResult<T>
            val body = response.body()
            result = if (response.isSuccessful && body != null) {
                RequestResult.SuccessResponse(body = body)
            } else {
                handleErrorResponse(response.errorBody()?.string().orEmpty())
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            callback.onResponse(proxy, Response.success(RequestResult.NetworkError(error = error)))
        }

        private fun handleErrorResponse(body: String): RequestResult.ErrorResponse {
            return RequestResult.ErrorResponse(ErrorResponseBody.byRawBody(body))
        }

    }

    override fun timeout(): Timeout {
        return proxy.timeout()
    }
}
