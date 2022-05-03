package com.co.mercapp.ui.viewmodel

import com.co.mercapp.ui.base.error.ErrorUIModel
import com.co.mercapp.ui.products.viewmodel.model.ProductDetailsUIModel
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel

val productModel = ProductUIModel(
    id = "ID20",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val productDetailsModel = ProductDetailsUIModel(
    availableQuantity = "1",
    soldQuantity = "1",
    warranty = "No warranty",
    pictures = emptyList(),
    attributes = emptyList()
)

val errorUIModel =
    ErrorUIModel(generalPurposeMessage = "Something went wrong", suggestedAction = "Retry")