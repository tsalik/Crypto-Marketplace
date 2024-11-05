package blog.tsalikis.marketplace.marketplace.datasource

import blog.tsalikis.marketplace.marketplace.datasource.network.BitfinexApi
import blog.tsalikis.marketplace.marketplace.domain.BitfinexTicker
import blog.tsalikis.marketplace.marketplace.domain.ContentError
import blog.tsalikis.marketplace.marketplace.domain.ErrorCase
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.math.BigDecimal
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

private const val PREFIX_FUNDING = "f"
private const val PREFIX_TICKER = "t"

private const val ERROR_CODE_RATE_LIMIT = 429

class TickerRepository @Inject constructor(private val bitfinexApi: BitfinexApi) {

    suspend fun getTickers(symbols: String): ContentError<List<BitfinexTicker>> {
        return try {
            val tickers = bitfinexApi.getTickers(symbols)
            val parsedTickers = tickers.mapNotNull { tickerValues ->
                val symbol = tickerValues[0] as String
                if (symbol.startsWith(PREFIX_FUNDING)) {
                    null
                } else {
                    val (from, to) = parseSymbols(symbol)
                    BitfinexTicker(
                        symbolFrom = from,
                        symbolTo = to,
                        lastPrice = BigDecimal(tickerValues[7].toString()),
                        dailyChangeRelative = BigDecimal(tickerValues[6].toString()),
                        iconUrl = "https://static.coincap.io/assets/icons/${from.lowercase()}@2x.png"
                    )
                }
            }
            ContentError.Success(parsedTickers)
        } catch (networkCallException: Exception) {
            parseHttpFailure(networkCallException)
        }
    }

    private fun parseSymbols(symbols: String): Pair<String, String> {
        val symbolsWithoutPrefix = symbols.removePrefix(PREFIX_TICKER)
        return when {
            symbolsWithoutPrefix.length == 6 -> {
                val splitIndex = symbolsWithoutPrefix.length / 2
                symbolsWithoutPrefix.substring(0, splitIndex) to symbolsWithoutPrefix.substring(
                    splitIndex
                )
            }

            else -> {
                val parts = symbolsWithoutPrefix.split(":")
                parts[0] to parts[1]
            }
        }
    }

    private fun parseHttpFailure(exception: Exception): ContentError<List<BitfinexTicker>> {
        return when (exception) {
            is UnknownHostException -> {
                ContentError.Error(ErrorCase.Connectivity)
            }

            is SocketTimeoutException -> {
                ContentError.Error(ErrorCase.Timeout)
            }

            is HttpException -> {
                val errorCode = exception.response()?.code()
                val errorCase = if (errorCode == ERROR_CODE_RATE_LIMIT) {
                    ErrorCase.Limit
                } else {
                    ErrorCase.Generic
                }
                return ContentError.Error(errorCase)
            }

            else -> {
                ContentError.Error(ErrorCase.Generic)
            }
        }
    }

}