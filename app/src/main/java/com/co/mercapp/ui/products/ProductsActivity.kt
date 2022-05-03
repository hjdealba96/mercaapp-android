package com.co.mercapp.ui.products

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.co.mercapp.R
import com.co.mercapp.ui.products.screens.ProductNavigationActions
import com.co.mercapp.ui.products.screens.ProductsNavigation
import com.co.mercapp.ui.products.viewmodel.ProductsViewModel
import com.co.mercapp.ui.theme.CornflowerBlue
import com.co.mercapp.ui.theme.MercaAppTheme
import com.co.mercapp.ui.utilities.openWebPage
import com.co.mercapp.ui.utilities.shareText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsActivity : ComponentActivity() {

    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MercaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CornflowerBlue
                ) {
                    ProductsNavigation(
                        searchState = viewModel.searchState.observeAsState(),
                        detailsState = viewModel.detailsState.observeAsState(),
                        productNavigationActions = setupActions()
                    )
                }
            }
        }
    }

    private fun setupActions(): ProductNavigationActions =
        ProductNavigationActions(
            doWhenShowProductDetails = { product -> viewModel.getProductDetails(product) },
            doWhenSearchActionClicked = { text -> viewModel.initialSearch(text) },
            doWhenMoreResultsRequested = { text -> viewModel.fetchMoreResults(text) },
            doWhenShareButtonClicked = { description ->
                shareText(title = getString(R.string.title_share_product), description)
            },
            doWhenStoreButtonClicked = { url ->
                openWebPage(title = getString(R.string.title_open_web_page), url = url)
            },
            doWhenBackButtonPressed = { onBackPressed() })

}