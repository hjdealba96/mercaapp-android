@file:OptIn(ExperimentalAnimationApi::class)

package com.co.mercapp.ui.products.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import com.co.mercapp.ui.compose.textFieldSaver
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel
import com.co.mercapp.ui.products.viewmodel.state.ProductDetailsState
import com.co.mercapp.ui.products.viewmodel.state.ProductsSearchState
import com.co.mercapp.ui.theme.CornflowerBlue
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ProductsNavigation(
    searchState: State<ProductsSearchState?>,
    detailsState: State<ProductDetailsState?>,
    productNavigationActions: ProductNavigationActions,
) {
    val navigationController = rememberAnimatedNavController()
    val systemUiController = rememberSystemUiController()

    val springSpec = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )

    var searchTextValue by rememberSaveable(stateSaver = textFieldSaver) {
        mutableStateOf(TextFieldValue())
    }

    AnimatedNavHost(navigationController, startDestination = Route.SEARCH.name) {
        composable(Route.FEATURES.name) {
            SetStatusBarColor(systemUiController = systemUiController, CornflowerBlue)
            ProductFeaturesScreen(state = detailsState.value,
                doWhenBackButtonClicked = {
                    productNavigationActions.doWhenBackButtonPressed()
                })
        }
        composable(Route.DETAILS.name) {
            SetStatusBarColor(systemUiController = systemUiController, CornflowerBlue)
            ProductDetailsScreen(
                state = detailsState.value,
                actions = productDetailsActions(navigationController, productNavigationActions)
            )
        }
        composable(Route.RESULTS.name,
            enterTransition = {
                slideInVertically(initialOffsetY = { 1000 }, animationSpec = springSpec)
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { 2000 }, animationSpec = springSpec)
            })
        {
            SetStatusBarColor(systemUiController = systemUiController, color = Color.White)
            SearchResultsScreen(
                searchTextValue = searchTextValue,
                searchState = searchState.value,
                actions = searchResultActions(
                    searchTextValue,
                    doWhenSearchedTextChanged = { text ->
                        searchTextValue = text
                                                },
                    productNavigationActions,
                    navigationController
                )
            )
        }
        composable(Route.SEARCH.name) {
            SetStatusBarColor(systemUiController = systemUiController, CornflowerBlue)
            SearchProductScreen(
                searchTextValue = searchTextValue,
                doWhenSearchedTextChanged = { text -> searchTextValue = text },
                doWhenSearchActionClicked = {
                    navigationController.navigate(Route.RESULTS.name)
                    productNavigationActions.doWhenSearchActionClicked(searchTextValue.text)
                }
            )
        }
    }
}

@Composable
private fun productDetailsActions(
    navigationController: NavHostController,
    productNavigationActions: ProductNavigationActions
) =
    ProductDetailsActions(
        doWhenBackButtonClicked = {
            productNavigationActions.doWhenBackButtonPressed()
        }, doWhenShowFeaturesClicked = {
            navigationController.navigate(Route.FEATURES.name)
        }, doWhenShareButtonClicked = { description ->
            productNavigationActions.doWhenShareButtonClicked(description)
        }, doWhenStoreButtonClicked = { link ->
            productNavigationActions.doWhenStoreButtonClicked(link)
        }, doWhenRetryLoadContent = { product ->
            productNavigationActions.doWhenShowProductDetails(product)
        })

@Composable
private fun searchResultActions(
    searchTextValue: TextFieldValue,
    doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    productNavigationActions: ProductNavigationActions,
    navigationController: NavHostController
) =
    SearchResultsActions(doWhenSearchedTextChanged = doWhenSearchedTextChanged,
        doWhenLoadingMoreItems = {
            productNavigationActions.doWhenMoreResultsRequested(
                searchTextValue.text
            )
        },
        doWhenBackButtonClicked = productNavigationActions.doWhenBackButtonPressed,
        doWhenSearchActionClicked = {
            productNavigationActions.doWhenSearchActionClicked(
                searchTextValue.text
            )
        },
        doOnSelectedProduct = { product ->
            productNavigationActions.doWhenShowProductDetails(product)
            navigationController.navigate(Route.DETAILS.name)
        })

@Composable
private fun SetStatusBarColor(systemUiController: SystemUiController, color: Color) {
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
}

data class ProductNavigationActions(
    val doWhenSearchActionClicked: (String) -> Unit,
    val doWhenMoreResultsRequested: (String) -> Unit,
    val doWhenShowProductDetails: (ProductUIModel) -> Unit,
    val doWhenShareButtonClicked: (String) -> Unit,
    val doWhenStoreButtonClicked: (String) -> Unit,
    val doWhenBackButtonPressed: () -> Unit
)

enum class Route {
    SEARCH, RESULTS, DETAILS, FEATURES
}