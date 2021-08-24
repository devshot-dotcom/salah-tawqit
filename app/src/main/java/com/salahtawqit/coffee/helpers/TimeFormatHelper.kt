package com.salahtawqit.coffee.helpers

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper to format time from 24-hour format to another based on a preferred format.
 *
 * @param preferredFormat [String]. Preferred time format of the user.
 * @param timeFormatKeys [MutableList]<[String]>. String array of the available time formats to
 * lookup from.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class TimeFormatHelper(
    private val preferredFormat: String,
    private val timeFormatKeys: Array<String>
) {
    private val twentyFourHFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val twelveHFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val twelveHFormatNoSuffix = SimpleDateFormat("hh:mm", Locale.getDefault())

    /**
     * @param twentyFourHTime [String]. String of some time in 24-hour format.
     * @return [String]. Time in preferred format.
     */
    fun getFormattedTime(twentyFourHTime: String?): String {

        twentyFourHTime?.let {
            //just a backdoor / guard clause
            val twentyFourHDate = getTwentyFourHDate(it) ?: return it

            return when(preferredFormat) {
                "0" -> twelveHFormat.format(twentyFourHDate)
                "1" -> it
                else -> twelveHFormatNoSuffix.format(twentyFourHDate)
            }
        } ?: return "06:00"
    }

    private fun getTwentyFourHDate(twentyFourHTime: String): Date? {
        //parse the 24h time to get a date object from it
        var twentyFourHDate: Date? = null

        try {
            twentyFourHDate = twentyFourHFormat.parse(twentyFourHTime)
        } catch (e: ParseException) {
            println(e.message)
        }

        return twentyFourHDate
    }
}