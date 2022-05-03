package com.co.mercapp.ui.compose

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.co.mercapp.ui.base.error.ErrorUIModel
import com.co.mercapp.ui.theme.AmaranthRed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ErrorSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
        Snackbar(
            contentColor = Color.White,
            actionColor = Color.White,
            backgroundColor = AmaranthRed,
            snackbarData = snackbarData
        )
    }
}

@Composable
fun ShowErrorSnackBar(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    error: ErrorUIModel,
    doWhenActionPerformed: () -> Unit
) {
    LaunchedEffect(key1 = snackbarHostState,
        block = {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = error.getWarningMessage(),
                    actionLabel = error.suggestedAction,
                    duration = SnackbarDuration.Indefinite
                )
                if (result == SnackbarResult.ActionPerformed) {
                    doWhenActionPerformed()
                }
            }
        })
}