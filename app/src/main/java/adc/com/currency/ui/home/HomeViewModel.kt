package adc.com.currency.ui.home

import adc.com.currency.common.UiState
import adc.com.currency.data.ADCRepository
import adc.com.currency.data.CurrencyDTO
import adc.com.currency.data.api.common.BaseResult
import adc.com.currency.utils.AppUtil
import android.util.Log
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
class HomeViewModel @Inject constructor(
    private val repository: ADCRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<CurrencyDTO>>> =
        MutableStateFlow(UiState.Init)
    val uiState: StateFlow<UiState<List<CurrencyDTO>>>
        get() = _uiState

    fun reloadDiscoverTodayCurrencyRates(){
        _uiState.value = UiState.Init
    }

    fun discoverTodayCurrencyRates(baseCurrency: String) {
        viewModelScope.launch {
            repository.discoverTodayCurrencyRates(baseCurrency)
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