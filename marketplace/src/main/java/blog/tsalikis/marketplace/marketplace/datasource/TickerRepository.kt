package blog.tsalikis.marketplace.marketplace.datasource

import blog.tsalikis.marketplace.marketplace.datasource.network.BitfinexApi
import blog.tsalikis.marketplace.marketplace.domain.BitfinexTicker
import blog.tsalikis.marketplace.marketplace.domain.ContentError
import blog.tsalikis.marketplace.marketplace.domain.ErrorCase
import javax.inject.Inject

class TickerRepository @Inject constructor(private val bitfinexApi: BitfinexApi) {

    suspend fun getTickers(symbols: String): ContentError<List<BitfinexTicker>> {
        return try {
            val tickers = bitfinexApi.getTickers(symbols)
            val parsedTickers = tickers.map { tickerValues ->
                val symbol = tickerValues[0] as String
                BitfinexTicker(
                    symbolFrom = parseSymbols(symbol).first,
                    symbolTo = parseSymbols(symbol).second,
                )
            }
            ContentError.Success(parsedTickers)
        } catch (networkCallException: Exception) {
            parseNetworkException(networkCallException)
        }
    }

    private fun parseSymbols(symbols: String): Pair<String, String> {
        val symbolsWithoutPrefix = symbols.removePrefix("t")
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

    private fun parseNetworkException(exception: Exception): ContentError.Error<List<BitfinexTicker>> {
        return ContentError.Error(ErrorCase.Generic)
    }

}