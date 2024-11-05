package blog.tsalikis.marketplace.marketplace.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import blog.tsalikis.marketplace.crypto.design.ShimmerPlaceholder
import blog.tsalikis.marketplace.marketplace.R
import blog.tsalikis.marketplace.marketplace.domain.ErrorCase

const val marketplaceDestination = "search/tokens"

fun NavGraphBuilder.marketplace() {
    composable(marketplaceDestination) {
        val viewModel = hiltViewModel<MarketPlaceViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        DisposableLifecycleObserver(
            onResume = { viewModel.startPolling() },
            onPause = { viewModel.stopPolling() }
        )
        MarketPlaceScreen(
            state = state,
            onTextChanged = { viewModel.filterFromSearched(it) },
            onRetry = { viewModel.onRetry() }
        )
    }
}

@Composable
fun DisposableLifecycleObserver(onResume: () -> Unit, onPause: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnResume by rememberUpdatedState(onResume)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnResume()
            }
            if (event == Lifecycle.Event.ON_PAUSE) {
                currentOnPause()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

}

@Composable
fun MarketPlaceScreen(
    state: MarketPlaceState,
    onTextChanged: (String) -> Unit,
    onRetry: () -> Unit,
) {
    when (state) {
        is MarketPlaceState.Error -> Scaffold { paddingValues ->
            val title = when (state.errorCase) {
                ErrorCase.Timeout -> TODO()
                ErrorCase.Connectivity -> R.string.no_connection_title
                ErrorCase.Generic -> TODO()
                ErrorCase.Limit -> TODO()
            }
            val subtitle = when (state.errorCase) {
                ErrorCase.Timeout -> TODO()
                ErrorCase.Connectivity -> R.string.no_connection_subtitle
                ErrorCase.Generic -> TODO()
                ErrorCase.Limit -> TODO()
            }
            ErrorScreen(
                title = stringResource(title),
                subtitle = stringResource(subtitle),
                onRetry = onRetry,
                modifier = Modifier.padding(paddingValues)
            )
        }

        MarketPlaceState.Loading -> Scaffold { padding ->
            Column(modifier = Modifier.padding(padding)) {
                ShimmerPlaceholder()
            }
        }

        is MarketPlaceState.Success -> {
            MarketplaceContent(state, onTextChanged)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarketplaceContent(state: MarketPlaceState.Success, onTextChanged: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = {
                            onTextChanged.invoke(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.search_ticker_placeholder)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_ticker_placeholder)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_search),
                                modifier = Modifier.clickable { onTextChanged("") }
                            )
                        },
                        singleLine = true
                    )
                },
                modifier = Modifier.padding(top = 8.dp, end = 16.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(items = state.values, key = { item -> item.symbolFrom }) { item ->
                TickerItem(item)
            }
        }
    }
}


@Composable
fun ErrorScreen(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}
