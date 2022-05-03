package com.co.mercapp.ui.products.viewmodel.state

import com.co.mercapp.ui.base.error.ErrorUIModel
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel

sealed class ProductsSearchState {
    object Loading : ProductsSearchState()
    object EmptySearchedText : ProductsSearchState()
    object NoResults : ProductsSearchState()
    class Results(
        val products: List<ProductUIModel>,
        val isRefreshing: Boolean = false,
        val refreshingError: ErrorUIModel? = null
    ) : ProductsSearchState()

    class Failure(val error: ErrorUIModel) : ProductsSearchState() {
        override fun equals(other: Any?): Boolean = other is Failure
                && other.error.getWarningMessage() == error.getWarningMessage()

        override fun hashCode(): Int = error.hashCode()
    }
}
