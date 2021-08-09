package com.salahtawqit.coffee.calculators

import com.salahtawqit.coffee.Utilities
import java.text.SimpleDateFormat
import java.util.*

class VoluntaryPrayersCalculator {
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) //24 hour format
    private val utilities = Utilities()
    private lateinit var dataMap: MutableMap<String, String>

    /**
     * Night, for us, starts at Maghrib and ends at Fajr.
     *
     * Therefore, the time between maghrib and fajr is called night time.
     * @return [Double]. Night time in hours.
     */
    private fun calculateNightTime(): Double {
        val maghribDate = timeFormat.parse(dataMap["Maghrib"] ?: "06:00")
        val fajrDate = timeFormat.parse(dataMap["Fajr"] ?: "06:00")

        if (maghribDate == null || fajrDate == null) {
            return 0.0
        }

        //night is the time between imsak and maghrib
        val nightTimeInMillis = (maghribDate.time - fajrDate.time).toDouble()

        //convert milliseconds into hours
        return nightTimeInMillis / (1000 * 60 * 60) % 24
    }

    /**
     * Calculate the time when tahajjud starts.
     *
     * Although tahajjud can be prayed after isha, the best time for it is the last part of the night.
     * Some say it's the 1/3 part of the night but a better explanation is to divide the night into
     * 7 parts and use the last part as the time for tahajjud.
     *
     * @return [String]. The time for tahajjud in a specific time format.
     */
    fun getTahajjudTime(): String {
        //hours to minutes
        val nightTimeInMinutes = (calculateNightTime() / 7 * 60).toInt()
        return utilities.subtractMinutes(
            minutes = nightTimeInMinutes,
            source = dataMap["Fajr"] ?: "06:00",
            format = timeFormat)
    }

    /**
     * Calculate the time when ishraq starts.
     *
     * Time for ishraq starts about 10-20 minutes after sunrise.
     *
     * @return [String]. The time for ishraq in a specific time format.
     */
    fun getIshraqTime(): String {
        // Get sunrise time.
        val sunrise = dataMap["Sunrise"] ?: "06:00"

        // Add 10 minutes to sunrise.
        return utilities.addMinutes(
            minutes = 20,
            source = sunrise,
            format = timeFormat)
    }

    /**
     * Calculate the time when duha starts.
     *
     * Time for duha starts the same as ishraq but, it's better to offer it at about 10-20 minutes
     * after ishraq or even better when offered sometimes before dhuhr.
     *
     * @return [String]. The time for duha in a specific time format.
     */
    fun getDuhaTime(): String {
        // Get ishraq time.
        val ishraq = getIshraqTime()

        // Add 10 minutes to ishraq.
        return utilities.addMinutes(
            minutes = 20,
            source = ishraq,
            format = timeFormat)
    }

    // Setters.
    fun setDataMap(dataMap: MutableMap<String, String>) {
        this.dataMap = dataMap
    }
}