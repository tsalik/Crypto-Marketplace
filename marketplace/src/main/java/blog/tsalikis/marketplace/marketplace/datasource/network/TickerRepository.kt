package blog.tsalikis.marketplace.marketplace.datasource.network

import blog.tsalikis.marketplace.marketplace.domain.ContentError
import blog.tsalikis.marketplace.marketplace.domain.ErrorCase
import javax.inject.Inject

class TickerRepository @Inject constructor(private val bitfinexApi: BitfinexApi) {

    suspend fun getTickers(symbols: String): ContentError<List<List<Any>>> {
        return try {
            val tickers = bitfinexApi.getTickers(symbols)
            ContentError.Success(tickers)
        } catch (networkCallException: Exception) {
            parseNetworkException(networkCallException)
        }
    }

    private fun parseNetworkException(exception: Exception): ContentError.Error<List<List<Any>>> {
        return ContentError.Error(ErrorCase.Generic)
    }

}