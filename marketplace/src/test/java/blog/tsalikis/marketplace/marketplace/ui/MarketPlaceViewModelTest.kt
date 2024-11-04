package blog.tsalikis.marketplace.marketplace.ui

import app.cash.turbine.test
import blog.tsalikis.marketplace.marketplace.datasource.TickerRepository
import blog.tsalikis.marketplace.marketplace.datasource.network.BitfinexApi
import blog.tsalikis.marketplace.marketplace.domain.BitfinexTicker
import blog.tsalikis.marketplace.util.CoroutineTestExtension
import blog.tsalikis.marketplace.util.InstantExecutorExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class, InstantExecutorExtension::class)
class MarketPlaceViewModelTest {

    private val bitfinexApi = mock<BitfinexApi>()
    private val tickerRepository = TickerRepository(bitfinexApi)
    private val viewModel by lazy { MarketPlaceViewModel(tickerRepository) }

    @Test
    fun `should show loading`() = runTest {
        whenever(bitfinexApi.getTickers(any())).thenReturn(
            listOf(
                listOf(
                    "tBTCUSD",
                    67956,
                    5.45609834,
                    67957,
                    6.2569596,
                    -517,
                    -0.00755042,
                    67956,
                    434.80983796,
                    69505,
                    67328,
                )
            )
        )

        viewModel.startPolling()

        viewModel.state.test {

            assertThat(awaitItem()).isEqualTo(
                MarketPlaceState.Success(
                    listOf(
                        BitfinexTicker(
                            symbolFrom = "BTC",
                            symbolTo = "USD",
                            lastPrice = BigDecimal("67956"),
                            dailyChangeRelative = BigDecimal("-0.00755042"),
                            iconUrl = "https://static.coincap.io/assets/icons/btc@2x.png"
                        )
                    )
                )
            )
        }

        viewModel.stopPolling()
    }

    @Test
    fun `should show error`() = runTest {
        whenever(bitfinexApi.getTickers(any())).thenThrow(RuntimeException())

        viewModel.startPolling()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(MarketPlaceState.Error)

            viewModel.stopPolling()
        }
    }

    @Test
    fun `should poll every 5 seconds`() = runTest {
        whenever(bitfinexApi.getTickers(any())).thenReturn(
            listOf(
                listOf(
                    "tBTCUSD",
                    67956,
                    5.45609834,
                    67957,
                    6.2569596,
                    -517,
                    -0.00755042,
                    67956,
                    434.80983796,
                    69505,
                    67328,
                )
            )
        )

        viewModel.startPolling()

        viewModel.state.test {

            awaitItem()

            advanceTimeBy(15000)

            verify(bitfinexApi, times(3)).getTickers(any())

            viewModel.stopPolling()
        }
    }
}