package adc.com.currency.utils

import adc.com.currency.data.api.common.BaseResult
import adc.com.currency.data.api.common.Failure
import android.content.Context
import android.location.Address
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import okhttp3.ResponseBody
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object AppUtil {
    fun formatCurrency(amount: Double, decimalDigits: Int): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = decimalDigits
        format.currency = Currency.getInstance("USD")
        return format.format(amount).replace("$", "")
    }

    fun getCountryName(currencyCode: String): String? {
        return when (currencyCode) {
            "AFN" -> "AFGHANISTAN"
            "DZD" -> "ALGERIA"
            "ARS" -> "ARGENTINA"
            "AMD" -> "ARMENIA"
            "AWG" -> "ARUBA"
            "AUD" -> "AUSTRALIA"
            "AZN" -> "AZERBAIJAN"
            "BSD" -> "BAHAMAS"
            "BHD" -> "BAHRAIN"
            "THB" -> "THAILAND"
            "PAB" -> "PANAMA"
            "BBD" -> "BARBADOS"
            "BYN" -> "BELARUS"
            "BZD" -> "BELIZE"
            "BMD" -> "BERMUDA"
            "VEF" -> "VENEZUELA"
            "BOB" -> "BOLIVIA"
            "BRL" -> "BRAZIL"
            "BND" -> "BRUNEI DARUSSALAM"
            "BGN" -> "BULGARIA"
            "BIF" -> "BURUNDI"
            "CVE" -> "CABO VERDE"
            "CAD" -> "CANADA"
            "KYD" -> "CAYMAN ISLANDS"
            "XOF" -> "SENEGAL"
            "XAF" -> "CAMEROON"
            "XPF" -> "FRENCH POLYNESIA"
            "CLP" -> "CHILE"
            "COP" -> "COLOMBIA"
            "KMF" -> "COMOROS"
            "CDF" -> "CONGO"
            "BAM" -> "BOSNIA AND HERZEGOVINA"
            "NIO" -> "NICARAGUA"
            "CRC" -> "COSTA RICA"
            "CUP" -> "CUBA"
            "CZK" -> "CZECH REPUBLIC"
            "GMD" -> "GAMBIA"
            "DKK" -> "DENMARK"
            "MKD" -> "REPUBLIC OF NORTH MACEDONIA"
            "DJF" -> "DJIBOUTI"
            "STN" -> "SAO TOME AND PRINCIPE"
            "DOP" -> "DOMINICAN REPUBLIC"
            "VND" -> "VIET NAM"
            "XCD" -> "ANGUILLA"
            "EGP" -> "EGYPT"
            "SVC" -> "EL SALVADOR"
            "ETB" -> "ETHIOPIA"
            "EUR" -> "EUROPE"
            "FJD" -> "FIJI"
            "HUF" -> "HUNGARY"
            "GHS" -> "GHANA"
            "GIP" -> "GIBRALTAR"
            "HTG" -> "HAITI"
            "PYG" -> "PARAGUAY"
            "GNF" -> "GUINEA"
            "GYD" -> "GUYANA"
            "HKD" -> "HONG KONG"
            "UAH" -> "UKRAINE"
            "ISK" -> "ICELAND"
            "INR" -> "INDIA"
            "IRR" -> "IRAN"
            "IQD" -> "IRAQ"
            "JMD" -> "JAMAICA"
            "JOD" -> "JORDAN"
            "KES" -> "KENYA"
            "PGK" -> "PAPUA NEW GUINEA"
            "LAK" -> "LAO PEOPLE’S DEMOCRATIC REPUBLIC"
            "HRK" -> "CROATIA"
            "KWD" -> "KUWAIT"
            "MWK" -> "MALAWI"
            "AOA" -> "ANGOLA"
            "MMK" -> "MYANMAR"
            "GEL" -> "GEORGIA"
            "LBP" -> "LEBANON"
            "ALL" -> "ALBANIA"
            "HNL" -> "HONDURAS"
            "SLE" -> "SIERRA LEONE"
            "LRD" -> "LIBERIA"
            "LYD" -> "LIBYA"
            "SZL" -> "SWAZILAND"
            "LSL" -> "LESOTHO"
            "MGA" -> "MADAGASCAR"
            "MYR" -> "MALAYSIA"
            "MUR" -> "MAURITIUS"
            "MXN" -> "MEXICO"
            "MXV" -> "MEXICO"
            "MDL" -> "MOLDOVA"
            "MAD" -> "MOROCCO"
            "MZN" -> "MOZAMBIQUE"
            "BOV" -> "BOLIVIA"
            "NGN" -> "NIGERIA"
            "ERN" -> "ERITREA"
            "NAD" -> "NAMIBIA"
            "NPR" -> "NEPAL"
            "ANG" -> "CURAÇAO"
            "ILS" -> "ISRAEL"
            "TWD" -> "TAIWAN"
            "NZD" -> "NEW ZEALAND"
            "BTN" -> "BHUTAN"
            "KPW" -> "NORTH KOREA"
            "NOK" -> "NORWAY"
            "PEN" -> "PERU"
            "MRU" -> "MAURITANIA"
            "TOP" -> "TONGA"
            "PKR" -> "PAKISTAN"
            "MOP" -> "MACAO"
            "CUC" -> "CUBA"
            "UYU" -> "URUGUAY"
            "PHP" -> "PHILIPPINES"
            "GBP" -> "UNITED KINGDOM"
            "BWP" -> "BOTSWANA"
            "QAR" -> "QATAR"
            "GTQ" -> "GUATEMALA"
            "ZAR" -> "SOUTH AFRICA"
            "OMR" -> "OMAN"
            "KHR" -> "CAMBODIA"
            "RON" -> "ROMANIA"
            "MVR" -> "MALDIVES"
            "IDR" -> "INDONESIA"
            "RUB" -> "RUSSIAN FEDERATION"
            "RWF" -> "RWANDA"
            "SHP" -> "SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA"
            "SAR" -> "SAUDI ARABIA"
            "XDR" -> "INTERNATIONAL MONETARY FUND"
            "RSD" -> "SERBIA"
            "SCR" -> "SEYCHELLES"
            "SGD" -> "SINGAPORE"
            "SBD" -> "SOLOMON ISLANDS"
            "KGS" -> "KYRGYZSTAN"
            "SOS" -> "SOMALIA"
            "TJS" -> "TAJIKISTAN"
            "SSP" -> "SOUTH SUDAN"
            "LKR" -> "SRI LANKA"
            "XSU" -> "SISTEMA UNITARIO DE COMPENSACION REGIONAL DE PAGOS SUCRE"
            "SDG" -> "SUDAN"
            "SRD" -> "SURINAME"
            "SEK" -> "SWEDEN"
            "CHF" -> "SWITZERLAND"
            "SYP" -> "SYRIAN ARAB REPUBLIC"
            "BDT" -> "BANGLADESH"
            "WST" -> "SAMOA"
            "TZS" -> "TANZANIA, UNITED REPUBLIC OF"
            "KZT" -> "KAZAKHSTAN"
            "TTD" -> "TRINIDAD AND TOBAGO"
            "MNT" -> "MONGOLIA"
            "TND" -> "TUNISIA"
            "TRY" -> "TURKEY"
            "TMT" -> "TURKMENISTAN"
            "AED" -> "UNITED ARAB EMIRATES"
            "UGX" -> "UGANDA"
            "CLF" -> "CHILE"
            "COU" -> "COLOMBIA"
            "UYI" -> "URUGUAY"
            "USD" -> "UNITED STATES OF AMERICA"
            "USN" -> "UNITED STATES OF AMERICA"
            "UZS" -> "UZBEKISTAN"
            "VUV" -> "VANUATU"
            "CHE" -> "SWITZERLAND"
            "CHW" -> "SWITZERLAND"
            "KRW" -> "KOREA"
            "YER" -> "YEMEN"
            "JPY" -> "JAPAN"
            "CNY" -> "CHINA"
            "ZMW" -> "ZAMBIA"
            "ZWL" -> "ZIMBABWE"
            "PLN" -> "POLAND"
            else -> null
        }
    }

    fun getAddressTitle(address: Address): String {
        val sb = StringBuilder()
        sb.append(address.featureName)
        sb.append(", ")
        sb.append(address.thoroughfare)
        return sb.toString()
    }

    fun getAddressDetail(address: Address): String {
        val sb = StringBuilder()
//        sb.append(address.thoroughfare)
//        sb.append(", ")
        sb.append(address.subLocality)
        sb.append(", ")
        sb.append(address.locality)
        sb.append(", ")
        sb.append(address.subAdminArea)
        sb.append(", ")
        sb.append(address.adminArea)
        sb.append(", ")
        sb.append(address.countryName)
        sb.append(", ")
        sb.append(address.postalCode)
        return sb.toString()
    }

    fun formatErrorMessage(result: BaseResult<Any, Failure>): String {
        return if (result is BaseResult.Error) {
            // 0 means no internet connection
            if (result.err.code != 0)
                "[Code: ${result.err.code}]\n${result.err.message}"
            else
                "No Internet Connection"
        } else {
            "Unknown error"
        }
    }

    fun extractErrorMessage(
        responseCode: Int,
        responseMessage: String,
        responseBody: ResponseBody?
    ): BaseResult.Error<Failure> {
        return try {
            val errorMsg =
                responseBody?.string()
            if (errorMsg != null) {
                BaseResult.Error(Failure(responseCode, errorMsg))
            } else {
                BaseResult.Error(Failure(responseCode, responseMessage))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseResult.Error(Failure(responseCode, responseMessage))
        }
    }

    fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, duration).show()
    }

    fun <A> A.toJson(): String? {
        return Gson().toJson(this)
    }

    fun <A> String.fromJson(type: Class<A>): A {
        return Gson().fromJson(this, type)
    }

    fun formatCurrency(amount: Long, currencySymbol: String = "Rp "): String {
        val format = DecimalFormat("#,###")
        return currencySymbol + format.format(amount).replace(oldChar = ',', newChar = '.')
    }

    fun getThisMonthFirstMonday(
        month: Int = getCurrentDate("MM").toInt(),
        year: Int = getCurrentDate("yyyy").toInt()
    ): Calendar {
        val start = Calendar.getInstance()
        start[Calendar.MONTH] = month - 1 // month is 0 based on calendar
        start[Calendar.YEAR] = year
        start[Calendar.DAY_OF_MONTH] = 1
        start.time // to avoid problems getTime make set changes apply

        while (start.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            start.add(Calendar.DATE, -1)

        return start
    }

    fun getThisMonthLastSaturday(
        month: Int = getCurrentDate("MM").toInt(),
        year: Int = getCurrentDate("yyyy").toInt()
    ): Calendar {
        val end = Calendar.getInstance()
        end[Calendar.MONTH] = month // next month
        end[Calendar.YEAR] = year
        end[Calendar.DAY_OF_MONTH] = 1
        end.add(Calendar.DATE, -1) // to get this month last date
        end.time // to avoid problems getTime make set changes apply

        while (end.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            end.add(Calendar.DATE, +1)

        return end
    }

    fun getThisMonthDays(
        month: Int = getCurrentDate("MM").toInt(),
        year: Int = getCurrentDate("yyyy").toInt()
    ): List<String> {
        val listDay = mutableListOf<String>()
        val start = getThisMonthFirstMonday(month, year)
        val end = getThisMonthLastSaturday(month, year)

        while (!start.after(end)) {
            listDay.add(printCalendar(start))
            start.add(Calendar.DATE, 1)
        }

        return listDay
    }

    fun printCalendar(calendar: Calendar, dateFormat: String = "dd-MM-yyyy"): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun toCalendar(input: String, dateFormat: String = "yyyy-MM-dd HH:mm"): Calendar {
        val cal = Calendar.getInstance()
        stringToDate(input, dateFormat)?.let {
            cal.time = it
        }
        return cal
    }

    fun isValidEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    fun getCurrentDate(dateFormat: String = "yyyy-MM-dd HH:mm"): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return formatter.format(Date())
    }

    fun getYesterdayDate(dateFormat: String = "yyyy-MM-dd HH:mm"): String {
        val yest = Calendar.getInstance()
        yest.add(Calendar.DATE, -1)
        return printCalendar(yest, dateFormat)
    }

    fun stringToDate(input: String, dateFormat: String = "yyyy-MM-dd HH:mm"): Date? {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return formatter.parse(input)
    }

    fun dateToString(date: Date, dateFormat: String = "yyyy-MM-dd HH:mm"): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return formatter.format(date)
    }

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
}