package com.co.mercapp.data.products.datasource.remote.model

data class ProductDetailsDataModel(
    val availableQuantity: Int?,
    val soldQuantity: Int?,
    val warranty: String?,
    val pictures: List<PictureModel>?,
    val attributes: List<AttributesModel>?
) {

    data class PictureModel(val secureUrl: String?)

    data class AttributesModel(val name: String?, val valueName: String?)

}