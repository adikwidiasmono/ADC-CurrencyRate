package adc.com.currency.ui.component

import adc.com.currency.navigation.NavigationItem
import adc.com.currency.navigation.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import adc.com.currency.R
import adc.com.currency.ui.theme.ADCCurrencyTheme
import adc.com.currency.ui.theme.App_Black
import adc.com.currency.ui.theme.App_Gray
import adc.com.currency.ui.theme.App_White

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val home = NavigationItem(
        title = stringResource(R.string.tab_home),
        icon = painterResource(R.drawable.ic_cottage),
        iconSelected = painterResource(R.drawable.ic_cottage_filled),
        screen = Screen.Home
    )
    val navigationItems = listOf(
        home,
        NavigationItem(
            title = stringResource(R.string.tab_conversion),
            icon = painterResource(R.drawable.ic_price_change),
            iconSelected = painterResource(R.drawable.ic_price_change_filled),
            screen = Screen.Conversion
        )
    )

    Column {
        Spacer(
            modifier = Modifier
                .height(0.5.dp)
                .fillMaxWidth()
                .background(color = App_Gray)
        )
        NavigationBar(
            modifier = modifier
                .height(56.dp),
            containerColor = App_White
        ) {
            navigationItems.map { item ->
                val selected = currentRoute == item.screen.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = if (selected) item.iconSelected else item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    selected = selected,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = App_White,
                        selectedIconColor = App_Black,
                        unselectedIconColor = App_Gray
                    )
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun BottomBarPreview() {
    ADCCurrencyTheme {
        Column {
            Spacer(
                modifier = Modifier
                    .height(64.dp)
            )
            BottomBar(rememberNavController())
        }
    }
}