package com.co.mercapp.ui.products.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.co.mercapp.R
import com.co.mercapp.ui.compose.RoundedSearchTextField
import com.co.mercapp.ui.theme.CornflowerBlue
import com.co.mercapp.ui.theme.MercaAppTheme
import com.co.mercapp.ui.theme.Typography

@Composable
fun SearchProductScreen(
    searchTextValue: TextFieldValue,
    doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    doWhenSearchActionClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CornflowerBlue)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(getMaxContentWidth()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.title_welcome_to_merca_app),
                style = Typography.h5
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.title_buy_everything_best_price)
            )
            RoundedSearchTextField(
                padding = PaddingValues(top = 32.dp),
                searchTextValue = searchTextValue,
                doWhenSearchedTextChanged = doWhenSearchedTextChanged,
                doWhenSearchActionClicked = doWhenSearchActionClicked
            )

            val isSearchEnabled = searchTextValue.text.isBlank().not()

            IconButton(modifier = Modifier
                .padding(top = 24.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .background(
                    color = if (isSearchEnabled) Color.White else Color.LightGray,
                    shape = CircleShape
                ),
                enabled = isSearchEnabled,
                onClick = {
                    focusManager.clearFocus(force = true)
                    doWhenSearchActionClicked()
                })
            {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = CornflowerBlue,
                    contentDescription = stringResource(id = R.string.title_search)
                )
            }
        }
    }
}

/**
 * It returns the percentage of the screen that represents the maximum width of the main
 * Composable parent which contains all the widgets hierarchy. Currently it returns:
 *
 * 65% for landscape orientation
 * 85% for portrait orientation
 *
 * @return percentage
 *
 * */

@Composable
private fun getMaxContentWidth(): Float {
    val localConfiguration = LocalConfiguration.current
    return if (localConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        0.65f
    } else {
        0.85f
    }
}

@Preview(name = "Portrait")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchProductScreenPreviewPortrait() {
    MercaAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SearchProductScreen(searchTextValue = TextFieldValue(),
                doWhenSearchedTextChanged = {},
                doWhenSearchActionClicked = {})
        }
    }
}