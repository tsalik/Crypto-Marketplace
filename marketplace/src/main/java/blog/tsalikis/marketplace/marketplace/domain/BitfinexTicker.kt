package blog.tsalikis.marketplace.marketplace.domain

import java.math.BigDecimal

data class BitfinexTicker(
    val symbolFrom: String,
    val symbolTo: String,
    val lastPrice: BigDecimal,
    val dailyChangeRelative: BigDecimal,
)