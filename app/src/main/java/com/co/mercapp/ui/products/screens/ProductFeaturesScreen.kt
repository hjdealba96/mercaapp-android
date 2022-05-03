package com.co.mercapp.ui.products.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.co.mercapp.R
import com.co.mercapp.ui.compose.BackButton
import com.co.mercapp.ui.products.screens.preview.product
import com.co.mercapp.ui.products.screens.preview.productDetails
import com.co.mercapp.ui.products.viewmodel.model.ProductDetailsUIModel
import com.co.mercapp.ui.products.viewmodel.state.ProductDetailsState
import com.co.mercapp.ui.theme.BrightGray
import com.co.mercapp.ui.theme.MercaAppTheme
import com.co.mercapp.ui.theme.Typography

@Composable
fun ProductFeaturesScreen(
    state: ProductDetailsState?,
    doWhenBackButtonClicked: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar {
            TopAppBar(title = { Text(text = stringResource(id = R.string.title_product_features)) },
                navigationIcon = {
                    BackButton(doWhenBackButtonClicked = doWhenBackButtonClicked)
                })
        }
    }) { paddingValues ->
        if (state != null && state is ProductDetailsState.DetailsReady) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = stringResource(id = R.string.title_general_features),
                        style = Typography.h6
                    )
                }
                itemsIndexed(state.details.attributes) { index, attribute ->
                    FeatureItem(
                        index = index,
                        isLastItem = index == state.details.attributes.lastIndex,
                        attribute = attribute
                    )
                }
            }

        }
    }
}

@Composable
fun FeatureItem(
    index: Int,
    isLastItem: Boolean,
    attribute: ProductDetailsUIModel.ProductAttribute
) {
    val isFirstItem = index == 0
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp, end = 16.dp,
                bottom = if (isLastItem) 16.dp else 0.dp
            )
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        if (isFirstItem) {
            Divider(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
        Row(
            modifier = Modifier
                .background(if (index % 2 == 0) Color.White else BrightGray)
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                text = attribute.name,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, end = 16.dp, bottom = 8.dp),
                text = attribute.value
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
        }
        if (isLastItem) {
            Divider(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

@Preview("Portrait")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun ProductFeaturesScreenPreview() {
    MercaAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            ProductFeaturesScreen(state =
            ProductDetailsState.DetailsReady(
                product = product,
                details = productDetails
            ), doWhenBackButtonClicked = {})
        }
    }
}