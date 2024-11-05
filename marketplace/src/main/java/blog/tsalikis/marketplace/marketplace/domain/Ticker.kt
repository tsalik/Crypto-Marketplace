package blog.tsalikis.marketplace.marketplace.domain

import java.math.BigDecimal

data class Ticker(
    val symbolFrom: String,
    val symbolTo: String,
    val lastPrice: BigDecimal,
    val dailyChangeRelative: BigDecimal,
    val iconUrl: String,
    val formattedValue: String,
    val formattedDailyChangeRelative: String,
)