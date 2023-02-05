package adc.com.currency.data.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ADCApi {
    @GET("{date}/currencies/{from_currency}/{to_currency}.min.json")
    suspend fun getExchangeRates(
        @Path("date") date: String, // yyyy-MM-dd
        @Path("from_currency") fromCurrencyCode: String,
        @Path("to_currency") toCurrencyCode: String
    ): Response<JsonObject>

    @GET("{date}/currencies/{currency}.min.json")
    suspend fun discoverCurrencyRates(
        @Path("date") date: String, // yyyy-MM-dd
        @Path("currency") currencyCode: String
    ): Response<JsonObject>
}