package blog.tsalikis.marketplace.marketplace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import blog.tsalikis.marketplace.crypto.design.shimmerBrush
import blog.tsalikis.marketplace.marketplace.R
import coil3.compose.SubcomposeAsyncImage

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
        MarketPlaceState.Loading -> Text("Loading")
        is MarketPlaceState.Success -> {
            Scaffold { padding ->
                LazyColumn(
                    contentPadding = padding,
                    verticalArrangement = spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(items = state.values, key = { item -> item.symbolFrom }) { item ->
                        OutlinedCard (
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(4.dp),
                                verticalAlignment = CenterVertically,
                            ) {
                                SubcomposeAsyncImage(
                                    model = item.iconUrl,
                                    contentDescription = null,
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(shimmerBrush())
                                                .align(Center)
                                        )
                                    },
                                    error = {
                                        Text(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(Color.LightGray)
                                                .align(Center),
                                            text = item.symbolFrom,
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp,
                                        )
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .align(CenterVertically),
                                    contentScale = ContentScale.Crop
                                )
                                Text(item.symbolFrom, modifier = Modifier.weight(1f).padding(horizontal = 4.dp))
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.End,
                                ) {
                                    Row {
                                        Text(item.symbolTo)
                                        Text(item.lastPrice.toString())
                                    }
                                    Text(item.dailyChangeRelative.toString())
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun TickerContent(state: MarketPlaceState.Success) {

}
