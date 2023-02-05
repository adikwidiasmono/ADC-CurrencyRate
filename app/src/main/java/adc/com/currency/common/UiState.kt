package adc.com.currency.common

sealed class UiState<out T : Any?> {
    object Init : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
}