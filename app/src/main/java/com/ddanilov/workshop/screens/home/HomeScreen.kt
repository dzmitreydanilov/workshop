package com.ddanilov.workshop.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddanilov.workshop.components.HomeScreenItem
import com.ddanilov.workshop.subscribeAndCollectStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeScreen(
    onNavigateDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val state by viewModel.subscribeAndCollectStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier.padding(4.dp),
        state = state,
        onNavigateDetails = onNavigateDetails
    )

    LaunchedEffect(Unit) {
        viewModel.collectNavigation()
            .collect { navigation ->
                when (navigation) {
                    is NavigateDetails -> onNavigateDetails()
                }
            }
    }
}

@Composable
fun HomeScreenContent(
    state: HomeState,
    onNavigateDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state is HomeState.Loading) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
    }
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = state.items, key = { it.hashCode() }) {
            HomeScreenItem(it, onNavigateDetails)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(state = HomeState.Loaded(persistentListOf("A", "B", "C")), {})
}
