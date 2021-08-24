package com.salahtawqit.coffee.helpers

import android.app.Application
import androidx.preference.PreferenceManager
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.calculators.PrayerTimesCalculator

/**
 * Class that provides helper methods to retrieve user preferences.
 *
 * @param application [Application]. The application context.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class PreferencesHelper(
    private val application: Application) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val calculator = PrayerTimesCalculator()

    fun getCalculationMethod(): Int {
        return calculator.Karachi
    }

    fun getJurisdiction(): Int {
        val preference = preferences.getString(application
            .getString(R.string.key_school), application.getString(R.string.school_default))

        return when(preference) {
            "0" -> calculator.Hanafi
            else -> calculator.Shafii
        }
    }

    fun getLatAdjustment(): Int {
        val preference = preferences
            .getString(application.getString(R.string.key_latitude_adjustment)
                , application.getString(R.string.latitude_adjustment_default))

        return when(preference) {
            "0" -> calculator.None
            "1" -> calculator.OneSeventh
            "2" -> calculator.MidNight
            else -> calculator.angleBased
        }
    }

    fun getTimeFormat(): String? {
        return preferences.getString(application
            .getString(R.string.key_time_format), application.getString(R.string.time_format_default))
    }

    fun getHijriDate(): String? {
        return preferences.getString(application
            .getString(R.string.key_hijri_adjustment)
            , application.getString(R.string.hijri_adjustment_default))
    }
}