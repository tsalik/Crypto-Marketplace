package blog.tsalikis.marketplace.marketplace.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import blog.tsalikis.marketplace.crypto.design.ShimmerPlaceholder

const val marketplaceDestination = "search/tokens"

fun NavGraphBuilder.marketplace() {
    composable(marketplaceDestination) {
        val viewModel = hiltViewModel<MarketPlaceViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        MarketPlaceScreen(state)
    }
}

@Composable
fun MarketPlaceScreen(state: MarketPlaceState) {
    when (state) {
        MarketPlaceState.Error -> Text("Error")
        MarketPlaceState.Loading -> ShimmerPlaceholder()
        is MarketPlaceState.Success -> {
            MarketplaceContent(state)
        }
    }
}

@Composable
private fun MarketplaceContent(state: MarketPlaceState.Success) {
    Scaffold { padding ->
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


