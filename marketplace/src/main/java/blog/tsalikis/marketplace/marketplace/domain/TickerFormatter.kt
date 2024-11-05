package blog.tsalikis.marketplace.marketplace.domain

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

class TickerFormatter @Inject constructor() {

    fun formatValue(ticker: String, value: BigDecimal): String {
        val currency = Currency.getInstance(ticker)
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        currencyFormat.currency = currency
        return currencyFormat.format(value)
    }

    fun formatAsPercentage(value: BigDecimal): String {
        val percentage = value.multiply(BigDecimal(100))
        val decimalFormat = DecimalFormat("#,##0.00'%'")
        return decimalFormat.format(percentage)
    }
}