package adc.com.currency.ui.component

import adc.com.currency.ui.theme.ADCCurrencyTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppChipGroup(
    modifier: Modifier = Modifier,
    datas: List<String>,
    selectedData: String? = null,
    listState: LazyListState = rememberLazyListState(),
    onSelectedChanged: (String) -> Unit
) {

    Column(modifier = modifier) {
        LazyRow(
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            items(datas) {
                FilterChip(
                    selected = selectedData == it,
                    onClick = {
                        onSelectedChanged(it)
                    },
                    label = { Text(text = it.uppercase()) },
                    interactionSource = remember { MutableInteractionSource() },
                    modifier = Modifier
                )
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun ChipGroupPreview() {
    val listData = arrayListOf<String>()
    listData.add("IDR")
    listData.add("CNY")
    listData.add("JPN")
    listData.add("USD")
    listData.add("EUR")
    listData.add("KWR")
    listData.add("SGD")
    listData.add("AUD")
    listData.add("MYR")

    ADCCurrencyTheme {
        AppChipGroup(datas = listData, selectedData = "AUD") {}
    }
}