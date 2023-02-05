package adc.com.currency.ui.conversion

import adc.com.currency.R
import adc.com.currency.common.UiState
import adc.com.currency.ui.component.AppChipGroup
import adc.com.currency.ui.theme.*
import adc.com.currency.utils.AppUtil
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun ConversionScreen(
    modifier: Modifier = Modifier,
    viewModel: ConversionViewModel = hiltViewModel()
) {
    var inputAmount by remember { mutableStateOf(0L) }
    var conversionResult by remember { mutableStateOf(0.0) }
    var isOnConversion by remember { mutableStateOf(false) }

    ConversionContent(
        modifier = modifier,
        conversionResult = conversionResult,
        isOnConversionProgress = isOnConversion,
        onConversion = { fromCurrencyCode, toCurrencyCode, amount ->
            inputAmount = amount
            viewModel.getTodayExchangeRates(fromCurrencyCode, toCurrencyCode)
        }
    )

    viewModel.uiState.collectAsState(initial = UiState.Init).value.let { uiState ->
        when (uiState) {
            is UiState.Init -> {}
            is UiState.Loading -> {
                isOnConversion = true
            }
            is UiState.Success -> {
                isOnConversion = false
                val todayRate = uiState.data
                conversionResult = inputAmount.times(todayRate)
            }
            is UiState.Error -> {
                isOnConversion = false
                AppUtil.showToast(LocalContext.current, uiState.errorMessage)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionContent(
    modifier: Modifier = Modifier,
    conversionResult: Double,
    isOnConversionProgress: Boolean = false,
    onConversion: (fromCurrencyCode: String, toCurrencyCode: String, amount: Long) -> Unit
) {
    val listNumpad = arrayListOf<String>()
    listNumpad.add("1")
    listNumpad.add("2")
    listNumpad.add("3")
    listNumpad.add("4")
    listNumpad.add("5")
    listNumpad.add("6")
    listNumpad.add("7")
    listNumpad.add("8")
    listNumpad.add("9")
    listNumpad.add("CLR")
    listNumpad.add("0")
    listNumpad.add("DEL")

    val listData = arrayListOf<String>()
    listData.add("idr")
    listData.add("cny")
    listData.add("jpn")
    listData.add("usd")
    listData.add("eur")
    listData.add("sgd")
    listData.add("aud")
    listData.add("myr")

    var inputAmount by remember { mutableStateOf(0L) }

    var fromBase by remember { mutableStateOf("idr") }
    var toBase by remember { mutableStateOf("usd") }

    var showFromBaseOption by remember { mutableStateOf(false) }
    var showToBaseOption by remember { mutableStateOf(false) }

    val listStateFromBase = rememberLazyListState()
    val listStateToBase = rememberLazyListState()

    // Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .background(color = App_White)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_home_title),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(id = R.string.tab_conversion),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, top = 16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = App_Purple_Light,
                contentColor = App_Black
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_forex),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        )
                        .size(36.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            val t = fromBase
                            fromBase = toBase
                            toBase = t
                        }
                )
                Spacer(
                    modifier = Modifier
                        .size(width = 2.dp, height = 232.dp)
                        .background(color = App_White)
                        .align(Alignment.CenterVertically)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 16.dp, bottom = 16.dp, end = 16.dp)
                    ) {
                        if (!showFromBaseOption)
                            SuggestionChip(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                onClick = {
                                    showFromBaseOption = !showFromBaseOption
                                    coroutineScope.launch {
                                        // Animate scroll to the n th item
                                        listStateFromBase.animateScrollToItem(
                                            index = listData.indexOf(fromBase)
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = fromBase.uppercase(),
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_round_360),
                                        colorFilter = ColorFilter.tint(App_Purple_Dark),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                            )
                        else
                            AppChipGroup(
                                datas = listData, selectedData = fromBase,
                                listState = listStateFromBase,
                                modifier = Modifier
                            ) { selectedCurrency ->
                                fromBase = selectedCurrency
                                showFromBaseOption = !showFromBaseOption
                            }

                        Text(
                            text = "You have",
                            color = App_Gray_Dark,
                            modifier = Modifier
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp)
                        ) {
                            Text(
                                text = fromBase.uppercase(),
                                color = App_Red,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                text = DecimalFormat("#,###.##").format(inputAmount),
                                color = App_Black,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                    Divider(modifier = Modifier, color = App_White, thickness = 2.dp)
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                    ) {
                        if (!showToBaseOption)
                            SuggestionChip(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                onClick = {
                                    showToBaseOption = !showToBaseOption
                                    coroutineScope.launch {
                                        // Animate scroll to the n th item
                                        listStateToBase.animateScrollToItem(
                                            index = listData.indexOf(toBase)
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = toBase.uppercase(),
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_round_360),
                                        colorFilter = ColorFilter.tint(App_Purple_Dark),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                            )
                        else
                            AppChipGroup(
                                datas = listData, selectedData = toBase,
                                listState = listStateToBase,
                                modifier = Modifier
                            ) { selectedCurrency ->
                                toBase = selectedCurrency
                                showToBaseOption = !showToBaseOption
                            }

                        Text(
                            text = "You get",
                            color = App_Gray_Dark,
                            modifier = Modifier
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp)
                        ) {
                            Text(
                                text = toBase.uppercase(),
                                color = App_Green,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                text = DecimalFormat("#,###.######")
                                    .format(conversionResult),
                                color = App_Black,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
        LazyVerticalGrid(
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .background(color = App_Purple_Light)
                .weight(1f)
        ) {
            items(listNumpad) { numpad ->
                Box(
                    modifier = Modifier
                        .background(color = App_White)
                        .aspectRatio(1.75f)
                        .clickable {
                            val sb = StringBuilder()
                            sb.clear()
                            sb.append(inputAmount)
                            when (numpad) {
                                "DEL" -> {
                                    if (sb.isNotEmpty())
                                        sb.setLength(sb.length - 1)
                                    if (sb.isEmpty())
                                        sb.append(0)

                                    inputAmount = sb
                                        .toString()
                                        .toLong()
                                }
                                "CLR" -> {
                                    sb.clear()
                                    sb.append(0)

                                    inputAmount = sb
                                        .toString()
                                        .toLong()
                                }
                                else -> {
                                    if (sb.length < 10) {
                                        sb.append(numpad)

                                        inputAmount = sb
                                            .toString()
                                            .toLong()
                                    }
                                }
                            }
                        }
                ) {
                    Text(
                        text = numpad,
                        fontWeight = FontWeight.Bold,
                        color = App_Gray_Dark,
                        fontSize = 30.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        ElevatedButton(
            onClick = {
                if (inputAmount > 0)
                    onConversion(fromBase, toBase, inputAmount)
            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = App_Purple_Dark,
                contentColor = App_White
            ),
            enabled = !isOnConversionProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            if (isOnConversionProgress)
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .size(32.dp)
                )
            else
                Text(
                    text = stringResource(R.string.convert).uppercase(),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
        }
    }
}

@Composable
@Preview(showBackground = false, heightDp = 760)
fun ConversionScreenPreview() {
    ADCCurrencyTheme {
        ConversionContent(conversionResult = 0.0) { _, _, _ -> }
    }
}