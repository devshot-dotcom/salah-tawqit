package com.salahtawqit.coffee.helpers

import com.salahtawqit.coffee.calculators.PrayerTimesCalculator

/** Temporary class. */
class PreferencesHelper {
    private lateinit var calculator: PrayerTimesCalculator

    fun getTimeFormat(): Int {
        return calculator.Time24
    }

    fun getCalculationMethod(): Int {
        return calculator.Karachi
    }

    fun getJurisdiction(): Int {
        return calculator.Hanafi
    }

    fun getLatAdjustment(): Int {
        return calculator.angleBased
    }

    fun setCalculator(calculator: PrayerTimesCalculator) {
        this.calculator = calculator
    }
}