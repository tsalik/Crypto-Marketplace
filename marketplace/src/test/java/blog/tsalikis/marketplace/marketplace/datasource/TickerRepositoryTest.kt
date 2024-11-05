package blog.tsalikis.marketplace.marketplace.datasource

import blog.tsalikis.marketplace.marketplace.datasource.network.BitfinexApi
import blog.tsalikis.marketplace.marketplace.domain.ContentError
import blog.tsalikis.marketplace.marketplace.domain.BitfinexTicker
import blog.tsalikis.marketplace.marketplace.domain.ErrorCase
import blog.tsalikis.marketplace.marketplace.domain.TickerFormatter
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.net.UnknownHostException

class TickerRepositoryTest {

    private val bitfinexApi = mock<BitfinexApi>()
    private val tickerRepository = TickerRepository(bitfinexApi, TickerFormatter())

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

    private val fundingUSD = listOf(
        "fUSD",
        0.00043854246575342464,
        0.00025100000000000003,
        60,
        43284954.71282123,
        0.0001726,
        2,
        55693.62705,
        -0.000129,
        -0.3395,
        0.000251,
        268548691.21375424,
        0.00049288,
        0.0000694,
        null,
        null,
        26992173.56154926
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
                            lastPrice = BigDecimal("67956"),
                            dailyChangeRelative = BigDecimal("-0.00755042"),
                            iconUrl = "https://static.coincap.io/assets/icons/btc@2x.png",
                            formattedValue = "$67,956.00"
                        ),
                        BitfinexTicker(
                            symbolFrom = "ETH",
                            symbolTo = "USD",
                            lastPrice = BigDecimal("2429.5"),
                            dailyChangeRelative = BigDecimal("-0.00832687"),
                            iconUrl = "https://static.coincap.io/assets/icons/eth@2x.png",
                            formattedValue = "$2,429.50"
                        ),
                        BitfinexTicker(
                            symbolFrom = "XAUT",
                            symbolTo = "USD",
                            lastPrice = BigDecimal("2742.2"),
                            dailyChangeRelative = BigDecimal("0.00029182"),
                            iconUrl = "https://static.coincap.io/assets/icons/xaut@2x.png",
                            formattedValue = "$2,742.20",
                        )
                    )
                )
            )
        }

    @Test
    fun `should ignore funding tickers`() = runTest {
        whenever(bitfinexApi.getTickers("ALL")).thenReturn(
            listOf(
                btcUsd,
                ethUsd,
                fundingUSD,
            )
        )

        val result = tickerRepository.getTickers("ALL")

        assertThat(result).isEqualTo(
            ContentError.Success(
                listOf(
                    BitfinexTicker(
                        symbolFrom = "BTC",
                        symbolTo = "USD",
                        lastPrice = BigDecimal("67956"),
                        dailyChangeRelative = BigDecimal("-0.00755042"),
                        iconUrl = "https://static.coincap.io/assets/icons/btc@2x.png",
                        formattedValue = "$67,956.00",
                    ),
                    BitfinexTicker(
                        symbolFrom = "ETH",
                        symbolTo = "USD",
                        lastPrice = BigDecimal("2429.5"),
                        dailyChangeRelative = BigDecimal("-0.00832687"),
                        iconUrl = "https://static.coincap.io/assets/icons/eth@2x.png",
                        formattedValue = "$2,429.50",
                    ),
                )
            )
        )
    }
}