package com.co.mercapp.data.products.datasource.remote.mapper

import com.co.mercapp.data.products.datasource.remote.model.ProductDataModel
import com.co.mercapp.data.products.extensions.getAsJsonObjectOrNull
import com.co.mercapp.data.products.extensions.getOrNull
import com.co.mercapp.data.products.json.JsonResponseMapper
import com.co.mercapp.data.products.pagination.PaginationUtils
import com.co.mercapp.domain.base.ErrorEntity
import com.co.mercapp.domain.base.PaginatedDataEntity
import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.products.entity.ProductEntity
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object JsonToPaginatedProductsData :
    JsonResponseMapper<JsonObject, PaginatedDataEntity<ProductEntity>>() {

    override fun map(
        responseCode: Int,
        json: JsonObject
    ): Result<PaginatedDataEntity<ProductEntity>> {
        val paging = json.getAsJsonObjectOrNull(PAGING_OBJECT_KEY)
        val total = paging?.getOrNull(TOTAL_OBJECT_KEY)?.asInt?.run {

            /**
             * API requires an access token for more than 1000 results :eyes:
             * */

            if (this > MAX_UNAUTHENTICATED_LIMIT) MAX_UNAUTHENTICATED_LIMIT else this
        }
        val limit = paging?.getOrNull(LIMIT_OBJECT_KEY)?.asInt
        return if (total != null && limit != null) {
            val pages = PaginationUtils.calculatePages(total, limit)
            Result.Success(
                PaginatedDataEntity(
                    total = total,
                    pages = pages,
                    items = mapJsonArray(json.getAsJsonArray(RESULTS_OBJECT_KEY))
                )
            )
        } else {
            Result.Failure(
                ErrorEntity.RemoteResponseError(
                    code = responseCode,
                    message = "No information provided for pagination"
                )
            )
        }
    }

    private fun mapJsonArray(jsonArray: JsonArray): List<ProductEntity> {
        return GsonBuilder()
            .registerTypeAdapter(
                ProductEntity.ProductConditionEntity::class.java,
                ProductConditionDeserializer
            )
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
            .fromJson<List<ProductDataModel>?>(
                jsonArray,
                object : TypeToken<List<ProductDataModel>>() {}.type
            ).map { productModel ->
                mapModelToEntity(productModel)
            }
    }

    private fun mapModelToEntity(productModel: ProductDataModel): ProductEntity =
        ProductEntity(
            id = productModel.id,
            title = productModel.title ?: "",
            price = productModel.price ?: 0.0,
            condition = productModel.condition
                ?: ProductEntity.ProductConditionEntity.UNKNOWN,
            permalink = productModel.permalink ?: "",
            thumbnail = productModel.thumbnail ?: "",
            freeShipping = productModel.shipping?.freeShipping ?: false,
            address = ProductEntity.ProductAddressEntity(
                city = productModel.address?.cityName ?: "",
                state = productModel.address?.stateName ?: ""
            )
        )

    private const val PAGING_OBJECT_KEY = "paging"
    private const val TOTAL_OBJECT_KEY = "total"
    private const val LIMIT_OBJECT_KEY = "limit"
    private const val RESULTS_OBJECT_KEY = "results"

    private const val MAX_UNAUTHENTICATED_LIMIT = 1000

    object ProductConditionDeserializer : JsonDeserializer<ProductEntity.ProductConditionEntity> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ProductEntity.ProductConditionEntity {
            return runCatching {
                json?.asString?.uppercase()
                    ?.run { ProductEntity.ProductConditionEntity.valueOf(this) }
            }.getOrNull() ?: ProductEntity.ProductConditionEntity.UNKNOWN
        }

    }

}