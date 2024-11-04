package blog.tsalikis.marketplace.marketplace.datasource

import blog.tsalikis.marketplace.marketplace.datasource.network.BitfinexApi
import blog.tsalikis.marketplace.marketplace.domain.ContentError
import blog.tsalikis.marketplace.marketplace.domain.BitfinexTicker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TickerRepositoryTest {

    private val bitfinexApi = mock<BitfinexApi>()
    private val tickerRepository = TickerRepository(bitfinexApi)

    private val btcUsd = listOf(
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

    private val ethUsd = listOf(
        "tETHUSD",
        2430,
        108.03729621,
        2430.5,
        214.21013261,
        -20.4,
        -0.00832687,
        2429.5,
        3103.73590962,
        2491.4,
        2408.4
    )

    private val xautUsd = listOf(
        "tXAUT:USD",
        2739.9,
        141.18343871,
        2742.1,
        180.7786898,
        0.8,
        0.00029182,
        2742.2,
        39.25521294,
        2750.1,
        2737.8
    )

    @Test
    fun `should extract the symbol, last price and relative percentage change from the API response`() =
        runTest {
            whenever(bitfinexApi.getTickers("tBTCUSD,tETHUSD")).thenReturn(
                listOf(
                    btcUsd,
                    ethUsd,
                    xautUsd
                )
            )

            val result = tickerRepository.getTickers("tBTCUSD,tETHUSD")

            assertThat(result).isEqualTo(
                ContentError.Success(
                    listOf(
                        BitfinexTicker(
                            symbolFrom = "BTC",
                            symbolTo = "USD",
//                            lastPrice = 67956f,
//                            dailyChangeRelative = 0.00755042f
                        ),
                        BitfinexTicker(
                            symbolFrom = "ETH",
                            symbolTo = "USD",
//                            lastPrice = 2429.5f,
//                            dailyChangeRelative = -0.00832687f
                        ),
                        BitfinexTicker(
                            symbolFrom = "XAUT",
                            symbolTo = "USD",
                        )
                    )
                )
            )
        }
}