package adc.com.currency.navigation

import androidx.compose.ui.graphics.painter.Painter

data class NavigationItem(
    val title: String,
    val icon: Painter,
    val iconSelected: Painter,
    val screen: Screen
)