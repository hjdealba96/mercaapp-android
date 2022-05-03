package com.co.mercapp.domain.products.entity

data class ProductEntity(
    val id: String,
    val title: String = "",
    val price: Double = 0.0,
    val condition: ProductConditionEntity,
    val permalink: String = "",
    val thumbnail: String = "",
    val address: ProductAddressEntity,
    val freeShipping: Boolean = false
) {

    enum class ProductConditionEntity {
        NEW, USED, UNKNOWN
    }

    data class ProductAddressEntity(val city: String = "", val state: String = "")

}