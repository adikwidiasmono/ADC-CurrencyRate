package adc.com.currency.ui.component

import adc.com.currency.data.CurrencyDTO
import adc.com.currency.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import adc.com.currency.R
import adc.com.currency.utils.AppUtil
import android.view.View
import androidx.compose.material3.Divider
import androidx.compose.ui.text.style.TextAlign
import java.text.DecimalFormat

@Composable
fun AppCardRates(
    modifier: Modifier = Modifier,
    data: CurrencyDTO,
    cardBackground: Color = App_Purple_Light,
    onCardClicked: (data: CurrencyDTO) -> Unit
) {
    var percentChange = data.rateToday.minus(data.rateYesterday)
    if (percentChange != 0.0)
        percentChange = percentChange.times(100.0).div(data.rateYesterday)

    percentChange = String.format("%.2f", percentChange).toDouble()

    var percentColor = App_Gray_Dark
    if (percentChange < 0) percentColor = App_Red
    if (percentChange > 0) percentColor = App_Green

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = cardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.clickable {
            onCardClicked(data)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            AsyncImageWithPlaceholder(
                imgUrl = data.countryFlagURL, modifier = Modifier
                    .size(height = 56.dp, width = 80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = data.countryName,
                        color = App_Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Top)
                            .weight(1f)
                            .padding(end = 16.dp)
                    )

                    Text(
                        text = "${DecimalFormat("0.#").format(percentChange)} %",
                        color = percentColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = data.code.uppercase(),
                        color = App_Purple_Dark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                            .weight(1f)
                    )

                    if (percentChange != 0.0) {
                        val yesterdayRate = if (data.rateYesterday > 1000)
                            AppUtil.formatCurrency(data.rateYesterday, 3)
                        else
                            AppUtil.formatCurrency(data.rateYesterday, 6)
                        Text(
                            text = yesterdayRate,
                            color = App_Gray,
                            fontSize = 20.sp,
                            maxLines = 1,
                            textAlign = TextAlign.End,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = if (percentChange < 0) R.drawable.down else R.drawable.up),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }

                    val todayRate = if (data.rateToday > 1000)
                        AppUtil.formatCurrency(data.rateToday, 3)
                    else
                        AppUtil.formatCurrency(data.rateToday, 6)
                    Text(
                        text = todayRate,
                        color = App_Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun CardRatesPreview() {
    ADCCurrencyTheme {
        AppCardRates(
            data = CurrencyDTO(
                code = "sgd",
                countryName = "Indonesia",
                countryFlagURL = "https://countryflagsapi.com/svg/Indonesia",
                rateToday = 20000.0,
                rateYesterday = 15000.0
            )
        ) {}
    }
}