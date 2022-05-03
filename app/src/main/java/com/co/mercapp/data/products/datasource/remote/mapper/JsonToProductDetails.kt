package com.co.mercapp.data.products.datasource.remote.mapper

import com.co.mercapp.data.products.datasource.remote.model.ProductDetailsDataModel
import com.co.mercapp.data.products.extensions.getAsJsonObjectOrNull
import com.co.mercapp.data.products.extensions.getOrNull
import com.co.mercapp.data.products.json.JsonResponseMapper
import com.co.mercapp.domain.base.ErrorEntity
import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.products.entity.ProductDetailsEntity
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

object JsonToProductDetails : JsonResponseMapper<JsonArray, ProductDetailsEntity>() {

    override fun map(responseCode: Int, json: JsonArray): Result<ProductDetailsEntity> {
        val item = json.firstOrNull()?.getAsJsonObjectOrNull()
        val body = item?.getAsJsonObjectOrNull(BODY_OBJECT_KEY)
        val requestCode = item?.getOrNull(CODE_OBJECT_KEY)?.asInt
        return if (body != null) {
            Result.Success(mapJsonObject(body).run {
                ProductDetailsEntity(availableQuantity = this.availableQuantity ?: 0,
                    soldQuantity = this.soldQuantity ?: 0,
                    warranty = this.warranty ?: "",
                    pictures = this.pictures?.mapNotNull { picture -> picture.secureUrl }
                        ?: emptyList(),
                    attributes = this.attributes?.mapNotNull { attribute ->
                        if (!attribute.name.isNullOrBlank() && !attribute.valueName.isNullOrBlank()) {
                            ProductDetailsEntity.ProductAttributesEntity(
                                name = attribute.name,
                                value = attribute.valueName
                            )
                        } else {
                            null
                        }
                    } ?: emptyList()
                )
            })
        } else {
            Result.Failure(
                ErrorEntity.RemoteResponseError(
                    code = requestCode ?: responseCode,
                    message = "Invalid response"
                )
            )
        }
    }

    private fun mapJsonObject(jsonObject: JsonObject): ProductDetailsDataModel {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
            .fromJson(
                jsonObject,
                object : TypeToken<ProductDetailsDataModel>() {}.type
            )
    }

    private const val BODY_OBJECT_KEY = "body"
    private const val CODE_OBJECT_KEY = "code"

}