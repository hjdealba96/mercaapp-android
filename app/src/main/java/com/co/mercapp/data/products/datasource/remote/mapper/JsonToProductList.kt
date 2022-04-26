package com.co.mercapp.data.products.datasource.remote.mapper

import com.co.mercapp.base.Mapper
import com.co.mercapp.domain.base.Error
import com.co.mercapp.domain.products.entity.ProductEntity
import com.google.gson.JsonObject
import retrofit2.Response
import com.co.mercapp.domain.base.Result
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object JsonToProductList : Mapper<Response<JsonObject>, Result<List<ProductEntity>>> {

    override fun map(input: Response<JsonObject>): Result<List<ProductEntity>> {
        val body = input.body()
        return if (input.isSuccessful && body != null && body.isJsonNull.not() && body.isJsonObject) {
            Result.Success(
                GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                    .fromJson(
                        body.asJsonObject.getAsJsonArray("results"),
                        object : TypeToken<List<ProductEntity>>() {}.type
                    )
            )
        } else {
            Result.Failure(
                Error.RemoteResponseError(
                    code = input.code(),
                    message = input.errorBody()?.string()
                )
            )
        }
    }

}