package com.co.mercapp.ui.products.screens.preview

import androidx.compose.ui.graphics.Color
import com.co.mercapp.ui.base.error.ErrorUIModel
import com.co.mercapp.ui.products.screens.ProductDetailsActions
import com.co.mercapp.ui.products.screens.SearchResultsActions
import com.co.mercapp.ui.products.viewmodel.model.ProductDetailsUIModel
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel
import com.co.mercapp.ui.products.viewmodel.state.ProductDetailsState
import com.co.mercapp.ui.products.viewmodel.state.ProductsSearchState

// ------------ Models used for rendering states ----------------- //

val error =
    ErrorUIModel(generalPurposeMessage = "Something went wrong", suggestedAction = "Retry")

val product = ProductUIModel(
    id = "s",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val product2 = ProductUIModel(
    id = "si",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val product3 = ProductUIModel(
    id = "sa",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val productList = listOf(product, product2, product3)

val attributes = listOf(
    ProductDetailsUIModel.ProductAttribute(name = "Color", value = "Black"),
    ProductDetailsUIModel.ProductAttribute(name = "Brand", value = "Generic"),
    ProductDetailsUIModel.ProductAttribute(name = "Weight", value = "23 kg"),
    ProductDetailsUIModel.ProductAttribute(name = "Height", value = "1 meter"),
    ProductDetailsUIModel.ProductAttribute(name = "Width", value = "3 meter")
)

val productDetails = ProductDetailsUIModel(
    warranty = "",
    pictures = listOf(
        ProductDetailsUIModel.ProductPicture("1", ""),
        ProductDetailsUIModel.ProductPicture("1", "")
    ),
    attributes = attributes,
    availableQuantity = "3",
    availableQuantityColor = Color.Gray,
    soldQuantity = "4 sold"
)

// ------------ UI states of Search results screen ----------------- //

val searchResultActions = SearchResultsActions(
    doWhenSearchedTextChanged = {},
    doWhenLoadingMoreItems = {},
    doWhenBackButtonClicked = {},
    doWhenSearchActionClicked = {},
    doOnSelectedProduct = {})

val searchResultsLoading = ProductsSearchState.Loading

val searchResultsNoResults = ProductsSearchState.NoResults

val searchResults = ProductsSearchState.Results(products = productList)

val searchResultsRefreshing = ProductsSearchState.Results(products = productList, isRefreshing = true)

val searchResultsRefreshingError =
    ProductsSearchState.Results(products = productList, refreshingError = error)

val searchResultsFailure = ProductsSearchState.Failure(
    ErrorUIModel(
        generalPurposeMessage = "Opps! Something went wrong &#128550;",
        suggestedAction = "Retry"
    )
)

// ------------ UI states of Product details screen ----------------- //

val productDetailActions = ProductDetailsActions(
    doWhenBackButtonClicked = {},
    doWhenShowFeaturesClicked = {},
    doWhenShareButtonClicked = {},
    doWhenStoreButtonClicked = {},
    doWhenRetryLoadContent = {})

val productDetailsLoading = ProductDetailsState.Loading(product)
val productDetailsReady = ProductDetailsState.DetailsReady(product, productDetails)
val productFailureReady = ProductDetailsState.Failure(product, error)


