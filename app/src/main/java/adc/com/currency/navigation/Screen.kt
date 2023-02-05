package adc.com.currency.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Conversion : Screen("conversion")
}
