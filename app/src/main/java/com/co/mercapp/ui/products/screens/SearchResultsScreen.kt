package com.co.mercapp.ui.products.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.co.mercapp.R
import com.co.mercapp.ui.compose.ErrorSnackbarHost
import com.co.mercapp.ui.compose.RectangleSearchTextField
import com.co.mercapp.ui.compose.ShowErrorSnackBar
import com.co.mercapp.ui.products.screens.preview.*
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel
import com.co.mercapp.ui.products.viewmodel.state.ProductsSearchState
import com.co.mercapp.ui.theme.GreenHaze
import com.co.mercapp.ui.theme.MercaAppTheme
import com.co.mercapp.ui.theme.Typography
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun SearchResultsScreen(
    searchTextValue: TextFieldValue,
    searchState: ProductsSearchState?,
    actions: SearchResultsActions
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var searchFocusState by remember { mutableStateOf(true) }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RectangleSearchTextField(
                searchTextValue,
                enabled = searchState != ProductsSearchState.Loading,
                actions.doWhenSearchedTextChanged,
                actions.doWhenSearchActionClicked,
                actions.doWhenBackButtonClicked,
                doWhenFocused = { searchFocusState = true },
                doWhenFocusLost = { searchFocusState = false }
            )
        },
        snackbarHost = { snackbarHostState -> ErrorSnackbarHost(snackbarHostState = snackbarHostState) })
    { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            when (searchState) {
                ProductsSearchState.Loading -> LoadingSection()
                ProductsSearchState.NoResults -> NoResultsFoundSection()
                is ProductsSearchState.Results -> {
                    ProductsListSection(
                        searchState.products,
                        searchState.isRefreshing,
                        actions.doWhenLoadingMoreItems,
                        actions.doOnSelectedProduct
                    )
                    searchState.refreshingError?.let { error ->
                        ShowErrorSnackBar(
                            coroutineScope,
                            scaffoldState.snackbarHostState,
                            error
                        ) {
                            actions.doWhenLoadingMoreItems()
                        }
                    }
                }
                is ProductsSearchState.Failure -> {
                    with(searchState.error) {
                        FailureSection(generalPurposeMessage)
                        if (!criticalMessage.isNullOrBlank()) {
                            ShowErrorSnackBar(
                                coroutineScope,
                                scaffoldState.snackbarHostState,
                                searchState.error
                            ) {
                                actions.doWhenSearchActionClicked()
                            }
                        }
                    }
                }
                else -> {}
            }
            if (searchFocusState) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {}
                )
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
    }
}

@Composable
private fun NoResultsFoundSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.10f))
        Text(
            text = stringResource(id = R.string.warning_no_results_found),
            style = Typography.h5
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(id = R.string.title_try_with_another_words)
        )
    }
}

@Composable
private fun FailureSection(message: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.fillMaxHeight(0.10f))
        Image(
            painter = painterResource(id = R.drawable.ic_exclamation_70),
            contentDescription = stringResource(id = R.string.title_warning)
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = message,
            style = Typography.h6
        )
    }
}

@Composable
private fun ProductsListSection(
    products: List<ProductUIModel>,
    isRefreshing: Boolean,
    doWhenLoadingMoreItems: () -> Unit,
    doOnSelectedProduct: (ProductUIModel) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState
    ) {
        items(items = products,
            key = { product -> product.id }) { product ->
            Row(
                modifier = Modifier.clickable { doOnSelectedProduct(product) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.picture)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_picture_90)
                        .error(R.drawable.ic_no_picture_90)
                        .build(),
                    contentDescription = stringResource(id = R.string.title_product_image),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(60.dp)
                )
                Column {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 16.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = product.name
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                        text = product.price,
                        style = Typography.h6,
                    )
                    if (product.freeShipping) {
                        Text(
                            modifier = Modifier.padding(
                                start = 8.dp,
                                top = 4.dp,
                                bottom = 16.dp
                            ),
                            text = stringResource(id = R.string.title_free_shipping),
                            style = Typography.caption,
                            color = GreenHaze
                        )
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Divider()
                }
            }
        }
        if (isRefreshing) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center,
                )
                {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                }
            }
        }
    }
    DetermineIfLoadMoreItems(isRefreshing, lazyListState) {
        doWhenLoadingMoreItems()
    }
}

/**
 *
 * Determine if the list should request for more data. It works by checking if the first item
 * of the [threshold] is visible, this is done by using a [derivedStateOf] in order to avoid
 * unnecessary recompositions. It keeps into account if the UI is already waiting for more
 * data ([isRefreshing]) in order to prevent generating true flags on this case.
 *
 *
 * A [LaunchedEffect] resides here along with a [snapshotFlow]  for detecting and notifying
 * the result of the validation mentioned earlier, these effects are launched during the first
 * composition and dispose when this function leaves the composition. The computation performed
 * through these effects basically listen to the state generated from [derivedStateOf] and convert
 * it as stream in order to trigger [doWhenLoadingMoreItems] avoiding invoking it multiple times.
 *
 * @param isRefreshing Notifies if the UI is already been refreshed
 * @param listState State of the list to validate
 * @param doWhenLoadingMoreItems
 * @param threshold Number of items that indicates at what point the UI should be populated
 *
 * */

@Composable
fun DetermineIfLoadMoreItems(
    isRefreshing: Boolean,
    listState: LazyListState,
    threshold: Int = 5,
    doWhenLoadingMoreItems: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            if (lastVisibleItem != null) {
                isRefreshing.not() && lastVisibleItem >= listState.layoutInfo.totalItemsCount - threshold
            } else {
                false
            }
        }
    }
    LaunchedEffect(true) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                if (isRefreshing.not()) {
                    doWhenLoadingMoreItems()
                }
            }
    }

}

data class SearchResultsActions(
    val doWhenSearchActionClicked: () -> Unit,
    val doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    val doWhenLoadingMoreItems: () -> Unit,
    val doWhenBackButtonClicked: () -> Unit,
    val doOnSelectedProduct: (ProductUIModel) -> Unit
)

@Preview(name = "LoadingStateState")
@Composable
fun SearchResultsScreenLoadingStateStatePreview() {
    BuildSearchResultsPreview(searchResultsLoading)
}


@Preview(name = "NoResultsState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenNoResultsStatePreview() {
    BuildSearchResultsPreview(searchResultsNoResults)
}

@Preview(name = "ResultsState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenResultsStatePreview() {
    BuildSearchResultsPreview(searchResults)
}

@Preview(name = "ResultsRefreshingState")
@Composable
fun SearchResultsScreenResultsRefreshingStatePreview() {
    BuildSearchResultsPreview(searchResultsRefreshing)
}

@Preview(name = "ResultsFailureState")
@Composable
fun SearchResultsScreenResultsFailureStatePreview() {
    BuildSearchResultsPreview(searchResultsRefreshingError)
}

@Preview(name = "FailureState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenFailureStatePreview() {
    BuildSearchResultsPreview(searchResultsFailure)
}

@Composable
fun BuildSearchResultsPreview(state: ProductsSearchState) {
    MercaAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SearchResultsScreen(
                searchTextValue = TextFieldValue(),
                actions = searchResultActions,
                searchState = state
            )
        }
    }
}