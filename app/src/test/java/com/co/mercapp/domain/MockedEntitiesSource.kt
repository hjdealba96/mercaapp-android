package com.co.mercapp.domain

import com.co.mercapp.domain.base.ErrorEntity
import com.co.mercapp.domain.products.entity.ProductDetailsEntity
import com.co.mercapp.domain.products.entity.ProductEntity

val productEntity = ProductEntity(
    id = "ID20",
    title = "Product",
    price = 20.0,
    condition = ProductEntity.ProductConditionEntity.NEW,
    permalink = "",
    thumbnail = "",
    address = ProductEntity.ProductAddressEntity(
        city = "Miami",
        state = "Florida"
    ),
    freeShipping = true,
)

val productDetailsEntity = ProductDetailsEntity(
    availableQuantity = 1,
    soldQuantity = 2,
    warranty = "No warranty"
)

val errorEntity = ErrorEntity.NoNetworkConnectionError