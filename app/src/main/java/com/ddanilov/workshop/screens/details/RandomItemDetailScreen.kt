package com.ddanilov.workshop.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddanilov.workshop.subscribeAndCollectStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun RandomItemDetailScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<RandomItemDetailsViewModel>()
    val state by viewModel.subscribeAndCollectStateWithLifecycle()

    RandomItemDetailsScreenContent(
        modifier = modifier,
        state = state,
        onRememberMeClick = { viewModel.dispatchEvent(RememberMeEvent) },
        onForgetMeClick = { viewModel.dispatchEvent(ForgetMeEvent) },
        onAlertDialogConfirmClick = { viewModel.dispatchEvent(AlertDialogConfirmClickEvent) },
        onAlertDialogDismissClick = { viewModel.dispatchEvent(AlertDialogDismissClickEvent) },
    )
}

@Composable
fun RandomItemDetailsScreenContent(
    state: RandomItemDetailState,
    onRememberMeClick: () -> Unit,
    onForgetMeClick: () -> Unit,
    onAlertDialogDismissClick: () -> Unit,
    onAlertDialogConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        ShowDialog(
            state = state,
            onDismissClick = onAlertDialogDismissClick,
            onConfirmClick = onAlertDialogConfirmClick
        )

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Details Screen")

                Text(text = "Item details")
                Text(
                    text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when " +
                        "an unknown printer took a galley of type and scrambled it to make a type specimen book."
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state is RandomItemDetailState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp, top = 8.dp)
                        )
                    }
                    if (state.isUserSaved) {
                        Button(
                            onClick = onForgetMeClick,
                            enabled = state !is RandomItemDetailState.Loading
                        ) {
                            Text(text = "Forget Me")
                        }
                    } else {
                        Button(
                            onClick = onRememberMeClick,
                            enabled = state !is RandomItemDetailState.Loading
                        ) {
                            Text(text = "Remember Me")
                        }
                    }
                }
            }
        }

        LaunchedEffect(key1 = state) {
            if (state is RandomItemDetailState.Error) {
                snackbarHostState.showSnackbar(
                    message = state.error,
                    actionLabel = "ok",
                    duration = SnackbarDuration.Indefinite,
                ).let {
                    if (it == SnackbarResult.Dismissed) {
                        // TODO handle it workshop
                    }
                }
            }
        }
    }
}

@Composable
fun ShowDialog(
    state: RandomItemDetailState,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (state is RandomItemDetailState.ShowAreYouSureDialog) {
        AlertDialog(
            onDismissRequest = onDismissClick,
            title = {
                Text(text = "Do you want to clear storage?")
            },
            text = {
                Text(text = "Are you sure?")
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmClick
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissClick
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
