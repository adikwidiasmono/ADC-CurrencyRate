package adc.com.currency.ui.conversion

import adc.com.currency.common.UiState
import adc.com.currency.data.ADCRepository
import adc.com.currency.data.CurrencyDTO
import adc.com.currency.data.api.common.BaseResult
import adc.com.currency.utils.AppUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val repository: ADCRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Double>> =
        MutableStateFlow(UiState.Init)
    val uiState: StateFlow<UiState<Double>>
        get() = _uiState

    fun getTodayExchangeRates(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            repository.getTodayExchangeRates(fromCurrency, toCurrency)
                .onStart {
                    _uiState.value = UiState.Loading
                }
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _uiState.value = UiState.Success(result.data)
                        }
                        is BaseResult.Error -> {
                            _uiState.value = UiState.Error(AppUtil.formatErrorMessage(result))
                        }
                    }
                }
        }
    }
}