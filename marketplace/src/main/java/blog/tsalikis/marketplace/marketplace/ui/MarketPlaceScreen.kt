package blog.tsalikis.marketplace.marketplace.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val marketplaceDestination = "search/tokens"

fun NavGraphBuilder.marketplace() {
    composable(marketplaceDestination) {
        val viewModel = hiltViewModel<MarketPlaceViewModel>()
        MarketPlaceScreen(viewModel.symbols)
    }
}

@Composable
fun MarketPlaceScreen(symbols: String) {
    Text("MarketPlace screen should show here for symbols:$symbols")
}
