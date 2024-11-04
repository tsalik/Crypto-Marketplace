package blog.tsalikis.marketplace.marketplace

import app.cash.turbine.test
import blog.tsalikis.marketplace.marketplace.datasource.network.BitfinexApi
import blog.tsalikis.marketplace.marketplace.datasource.network.TickerRepository
import blog.tsalikis.marketplace.marketplace.ui.MarketPlaceState
import blog.tsalikis.marketplace.marketplace.ui.MarketPlaceViewModel
import blog.tsalikis.marketplace.util.CoroutineTestExtension
import blog.tsalikis.marketplace.util.InstantExecutorExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class, InstantExecutorExtension::class)
class MarketPlaceViewModelTest {

    private val bitfinexApi = mock<BitfinexApi>()
    private val tickerRepository = TickerRepository(bitfinexApi)
    private val viewModel by lazy { MarketPlaceViewModel(tickerRepository) }

    @Test
    fun `should show loading`() = runTest {
        whenever(bitfinexApi.getTickers("ALL")).thenReturn(listOf(listOf("1", 2, 3)))
        viewModel.state.test {

            assertThat(awaitItem()).isEqualTo(
                MarketPlaceState.Success(listOf(listOf("1", 2, 3)))
            )
        }
    }

    @Test
    fun `should show error`() = runTest {
        whenever(bitfinexApi.getTickers("ALL")).thenThrow(RuntimeException())

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(MarketPlaceState.Error)
        }
    }
}