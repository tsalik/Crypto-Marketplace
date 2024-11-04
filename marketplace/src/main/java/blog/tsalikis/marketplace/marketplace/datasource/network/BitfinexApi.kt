package blog.tsalikis.marketplace.marketplace.datasource.network

import retrofit2.http.GET
import retrofit2.http.Query

interface BitfinexApi {

    @GET("tickers")
    suspend fun getTickers(
        @Query("symbols") symbols: String // Comma-separated list, e.g., "tBTCUSD,tETHUSD"
    ): List<List<Any>>
}