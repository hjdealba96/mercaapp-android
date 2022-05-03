package com.co.mercapp.ui.products.viewmodel.model

import androidx.compose.ui.graphics.Color

data class ProductDetailsUIModel(
    val availableQuantity: String,
    val availableQuantityColor: Color = Color.LightGray,
    val soldQuantity: String,
    val warranty: String,
    val pictures: List<ProductPicture>,
    val attributes: List<ProductAttribute>
) {

    data class ProductPicture(
        val index: String,
        val url: String
    )

    data class ProductAttribute(
        val name: String,
        val value: String
    )

}