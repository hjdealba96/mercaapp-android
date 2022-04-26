package com.co.mercapp.domain.products.entity

data class ProductEntity(
    val id: String,
    val title: String,
    val price: Double,
    val currencyId: String,
    val availableQuantity: Int = 0,
    val soldQuantity: Int = 0,
    val productCondition: ProductCondition,
    val permaLink: String,
    val thumbnail: String,
    val acceptsMercadoPago: Boolean = false,
    val shipping: ProductShipping,
    val domainId: String,
    val tags: ArrayList<String>? = null
)