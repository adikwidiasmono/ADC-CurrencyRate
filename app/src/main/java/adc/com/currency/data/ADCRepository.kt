package adc.com.currency.data

import adc.com.currency.data.api.ADCRemoteSource
import adc.com.currency.data.api.common.BaseResult
import adc.com.currency.data.api.common.Failure
import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ADCRepository constructor(
    private val remoteSource: ADCRemoteSource
) {
    fun discoverTodayCurrencyRates(baseCurrency: String)
            : Flow<BaseResult<List<CurrencyDTO>, Failure>> {
        return flow {
            emit(remoteSource.discoverTodayCurrencyRates(baseCurrency))
        }
    }

    fun getTodayExchangeRates(fromCurrency: String, toCurrency: String)
            : Flow<BaseResult<Double, Failure>> {
        return flow {
            emit(remoteSource.getTodayExchangeRates(fromCurrency, toCurrency))
        }
    }
}