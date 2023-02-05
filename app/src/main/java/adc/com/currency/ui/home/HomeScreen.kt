package adc.com.currency.ui.home

import adc.com.currency.BuildConfig
import adc.com.currency.R
import adc.com.currency.common.UiState
import adc.com.currency.data.CurrencyDTO
import adc.com.currency.ui.component.AppCardRates
import adc.com.currency.ui.component.AppChipGroup
import adc.com.currency.ui.theme.ADCCurrencyTheme
import adc.com.currency.ui.theme.App_White
import adc.com.currency.utils.AppUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (currencyDTO: CurrencyDTO) -> Unit
) {
    var selectedBaseCurrency by remember { mutableStateOf("idr") }
    val listCurrencyData = remember { mutableStateListOf<CurrencyDTO>() }

    HomeContent(
        modifier = modifier,
        selectedBaseCurrency = selectedBaseCurrency,
        ratesData = listCurrencyData,
        navigateToDetail = navigateToDetail,
        onBaseCurrencyChanged = { baseCurrency ->
            selectedBaseCurrency = baseCurrency
            viewModel.reloadDiscoverTodayCurrencyRates()
        }
    )

    viewModel.uiState.collectAsState(initial = UiState.Init).value.let { uiState ->
        when (uiState) {
            is UiState.Init -> {
                viewModel.discoverTodayCurrencyRates(selectedBaseCurrency)
            }
            is UiState.Loading -> {}
            is UiState.Success -> {
                listCurrencyData.clear()
                listCurrencyData.addAll(uiState.data)
            }
            is UiState.Error -> {
                AppUtil.showToast(LocalContext.current, uiState.errorMessage)
            }
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    selectedBaseCurrency: String,
    ratesData: List<CurrencyDTO>,
    navigateToDetail: (currencyDTO: CurrencyDTO) -> Unit,
    onBaseCurrencyChanged: (currencyCode: String) -> Unit
) {
    val listData = arrayListOf<String>()
    listData.add("idr")
    listData.add("cny")
    listData.add("jpn")
    listData.add("usd")
    listData.add("eur")

    Column(
        modifier = modifier
            .background(color = App_White)
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_conversion_title),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(id = R.string.tab_home),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        AppChipGroup(
            datas = listData, selectedData = selectedBaseCurrency,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) { selectedCurrency ->
            onBaseCurrencyChanged(selectedCurrency)
        }
        Text(
            text = stringResource(id = R.string.today_rate),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp, top = 8.dp, start = 16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ratesData) { rate ->
                AppCardRates(
                    data = rate
                ) {
                    navigateToDetail(it)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun HomeScreenPreview() {
    val listRate = arrayListOf<CurrencyDTO>()
    listRate.add(
        CurrencyDTO(
            code = "YPN",
            countryName = "Japan",
            countryFlagURL = BuildConfig.BASE_IMAGE_URL + "Japan",
            rateToday = 15000.0,
            rateYesterday = 10000.0
        )
    )
    listRate.add(
        CurrencyDTO(
            code = "SGD",
            countryName = "Singapore",
            countryFlagURL = BuildConfig.BASE_IMAGE_URL + "Singapore",
            rateToday = 14500.0,
            rateYesterday = 20000.0
        )
    )
    listRate.add(
        CurrencyDTO(
            code = "USD",
            countryName = "United State of America",
            countryFlagURL = BuildConfig.BASE_IMAGE_URL + "United State of America",
            rateToday = 30000.0,
            rateYesterday = 30000.0
        )
    )

    ADCCurrencyTheme {
        HomeContent(
            selectedBaseCurrency = "idr",
            ratesData = listRate,
            navigateToDetail = {},
            onBaseCurrencyChanged = {})
    }
}