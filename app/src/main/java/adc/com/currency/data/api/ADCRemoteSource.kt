package adc.com.currency.data.api

import adc.com.currency.BuildConfig
import adc.com.currency.data.CurrencyDTO
import adc.com.currency.data.api.common.BaseResult
import adc.com.currency.data.api.common.Failure
import adc.com.currency.data.api.common.exception.NoInternetConnectionException
import adc.com.currency.utils.AppUtil
import android.util.Log
import com.google.gson.JsonObject

class ADCRemoteSource constructor(private val api: ADCApi) {

    suspend fun discoverTodayCurrencyRates(baseCurrency: String): BaseResult<List<CurrencyDTO>, Failure> {
        try {
            val respToday = api.discoverCurrencyRates(
                AppUtil.getCurrentDate("yyyy-MM-dd"), baseCurrency
            )
            return if (respToday.isSuccessful) {
                val resultToday = respToday.body()
                if (resultToday != null) {
                    val respYesterday = api.discoverCurrencyRates(
                        AppUtil.getYesterdayDate("yyyy-MM-dd"), baseCurrency
                    )
                    return if (respYesterday.isSuccessful) {
                        val resultYesterday = respYesterday.body()
                        if (resultYesterday != null) {
                            val ratesToday = resultToday.get(baseCurrency).asJsonObject
                            val ratesYesterday = resultYesterday.get(baseCurrency).asJsonObject
                            val listRates = arrayListOf<CurrencyDTO>()

                            for (data in ratesToday.entrySet()) {
                                AppUtil.getCountryName(data.key.uppercase())?.let {
                                    listRates.add(
                                        CurrencyDTO(
                                            code = data.key.uppercase(),
                                            countryName = it,
                                            countryFlagURL = BuildConfig.BASE_IMAGE_URL + it,
                                            rateToday = data.value?.asDouble ?: 0.0,
                                            rateYesterday = ratesYesterday.get(data.key)?.asDouble
                                                ?: 0.0
                                        )
                                    )
                                }
                            }
                            BaseResult.Success(listRates)
                        } else {
                            BaseResult.Error(Failure(respYesterday.code(), respYesterday.message()))
                        }
                    } else {
                        AppUtil.extractErrorMessage(
                            respYesterday.code(), respYesterday.message(), respYesterday.errorBody()
                        )
                    }


                } else {
                    BaseResult.Error(Failure(respToday.code(), respToday.message()))
                }
            } else {
                AppUtil.extractErrorMessage(
                    respToday.code(), respToday.message(), respToday.errorBody()
                )
            }
        } catch (e: NoInternetConnectionException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun getTodayExchangeRates(
        fromCurrency: String, toCurrency: String
    ): BaseResult<Double, Failure> {
        try {
            val response = api.getExchangeRates(
                AppUtil.getCurrentDate("yyyy-MM-dd"), fromCurrency, toCurrency
            )
            return if (response.isSuccessful) {
                val result = response.body()
                if (result != null) {
                    val exchangeRate = result.get(toCurrency).asDouble
                    BaseResult.Success(exchangeRate)
                } else {
                    BaseResult.Error(Failure(response.code(), response.message()))
                }
            } else {
                AppUtil.extractErrorMessage(
                    response.code(), response.message(), response.errorBody()
                )
            }
        } catch (e: NoInternetConnectionException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }
}