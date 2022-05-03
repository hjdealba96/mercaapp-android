package com.co.mercapp.data.products.json

import com.co.mercapp.data.products.extensions.errorBodyAsJsonObject
import com.co.mercapp.data.products.extensions.getOrNull
import com.co.mercapp.domain.base.ErrorEntity
import com.co.mercapp.domain.base.Mapper
import com.co.mercapp.domain.base.Result
import com.google.gson.JsonElement
import retrofit2.Response

abstract class JsonResponseMapper<I : JsonElement, T> : Mapper<Response<I>, Result<T>> {

    final override fun map(input: Response<I>): Result<T> {
        val rawBody = input.body()
        return if (input.isSuccessful && rawBody != null && rawBody.isJsonNull.not()) {
            map(responseCode = input.code(), json = rawBody)
        } else {
            val error = input.errorBodyAsJsonObject()?.getOrNull(ERROR_OBJECT_KEY)?.asString
            Result.Failure(
                ErrorEntity.RemoteResponseError(
                    code = input.code(),
                    message = error
                )
            )
        }
    }

    abstract fun map(responseCode: Int, json: I): Result<T>

    companion object {
        private const val ERROR_OBJECT_KEY = ""
    }

}