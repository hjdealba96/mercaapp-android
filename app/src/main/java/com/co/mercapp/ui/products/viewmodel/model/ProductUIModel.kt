package com.co.mercapp.ui.products.viewmodel.model

data class ProductUIModel(
    val id: String,
    val name: String,
    val price: String,
    val picture: String,
    val freeShipping: Boolean,
    val condition: String,
    val address: String,
    val shareableDescription: String,
    val storeLink: String
)
