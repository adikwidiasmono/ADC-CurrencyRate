package adc.com.currency.data.api.common.exception

import adc.com.currency.R
import android.content.Context
import okio.IOException

class NoInternetConnectionException(private val appContext: Context) : IOException() {
    override val message: String
        get() = appContext.getString(R.string.err_offline)
}