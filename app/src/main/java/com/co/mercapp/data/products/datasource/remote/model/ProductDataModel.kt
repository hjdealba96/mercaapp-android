package com.co.mercapp.data.products.datasource.remote.model

import com.co.mercapp.domain.products.entity.ProductEntity

class ProductDataModel(
    val id: String,
    val title: String?,
    val price: Double?,
    val condition: ProductEntity.ProductConditionEntity?,
    val permalink: String?,
    val thumbnail: String?,
    val address: AddressModel?,
    val shipping: ProductShippingModel?
) {
    data class AddressModel(
        val stateName: String?,
        val cityName: String?
    )

    data class ProductShippingModel(val freeShipping: Boolean?)

}