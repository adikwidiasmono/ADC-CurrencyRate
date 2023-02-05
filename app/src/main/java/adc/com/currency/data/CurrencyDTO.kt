package adc.com.currency.data

data class CurrencyDTO(
    val code: String,
    val countryName: String,
    val countryFlagURL: String,
    val rateToday: Double,
    val rateYesterday: Double
)
