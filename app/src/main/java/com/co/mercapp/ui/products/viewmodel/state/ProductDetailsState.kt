package com.co.mercapp.ui.products.viewmodel.state

import com.co.mercapp.ui.base.error.ErrorUIModel
import com.co.mercapp.ui.products.viewmodel.model.ProductDetailsUIModel
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel

sealed class ProductDetailsState(val product: ProductUIModel) {

    class Loading(product: ProductUIModel) : ProductDetailsState(product) {
        override fun equals(other: Any?): Boolean = other is Loading
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class DetailsReady(product: ProductUIModel, val details: ProductDetailsUIModel) :
        ProductDetailsState(product) {
        override fun equals(other: Any?): Boolean = other is DetailsReady
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class Failure(product: ProductUIModel, val error: ErrorUIModel) : ProductDetailsState(product) {
        override fun equals(other: Any?): Boolean = other is Failure
        override fun hashCode(): Int = javaClass.hashCode()
    }
}

