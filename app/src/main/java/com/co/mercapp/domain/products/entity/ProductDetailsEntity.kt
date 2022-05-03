package com.co.mercapp.domain.products.entity

data class ProductDetailsEntity(
    val availableQuantity: Int = 0,
    val soldQuantity: Int = 0,
    val attributes: List<ProductAttributesEntity> = emptyList(),
    val pictures: List<String> = emptyList(),
    val warranty: String = ""
) {
    data class ProductAttributesEntity(val name: String = "", val value: String = "")

}