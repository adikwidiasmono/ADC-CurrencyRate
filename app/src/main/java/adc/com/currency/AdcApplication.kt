package adc.com.currency

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AdcApplication : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        private var instance: AdcApplication? = null

        fun getInstance(): AdcApplication? {
            return instance
        }

        fun getContext(): Context? {
            return instance?.applicationContext
        }
    }
}