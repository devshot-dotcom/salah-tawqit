package com.salahtawqit.coffee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.salahtawqit.coffee.helpers.RoomDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Perform necessary operations on the [dataMap], store the values in the database, and wait for the
 * referring fragment to automatically update the UI using data binding utilities.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class PrayerTimesViewModel(application: Application): AndroidViewModel(application) {
    var dataMap = HashMap<String, String>()

    /**
     * Use Room to store data in the database.
     */
    fun storeData() {
        // Database access must be limited to a background thread hence we use coroutines.
        viewModelScope.launch(Dispatchers.IO) {
            val dao = RoomDatabaseHelper.getRoom(getApplication()).getCalculationResultsDao()
            val calculationResults = RoomDatabaseHelper.CalculationResults(
                id = 1,
                latitude = dataMap["lat"],
                longitude = dataMap["lon"],
                city = dataMap["city"],
                country = dataMap["country"],
                tahajjud = dataMap["Tahajjud"],
                fajr = dataMap["Fajr"],
                sunrise = dataMap["Sunrise"],
                ishraq = dataMap["Ishraq"],
                duha = dataMap["Duha"],
                dhuhr = dataMap["Dhuhr"],
                asr = dataMap["Asr"],
                sunset = dataMap["Sunset"],
                maghrib = dataMap["Maghrib"],
                isha = dataMap["Isha"],
            )

            // Select all rows.
            val selectionResult = dao.selectAll()

            // Insert a new row if empty.
            if(selectionResult.isEmpty()) dao.insert(calculationResults)

            // Update otherwise.
            else dao.update(calculationResults)
        }
    }

    /**
     * Calculate the Hijri date and add it to the dataMap.
     */
    fun calculateHijriDate() {
        val calendar = UmmalquraCalendar()
        val format = SimpleDateFormat("d MMMM, y", Locale.ENGLISH)
        format.calendar = calendar

        dataMap["hijri"] = format.format(calendar.time)
    }

    /**
     * Calculate the Gregorian date and add it to the dataMap.
     */
    fun calculateGregorianDate() {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("d MMMM, y", Locale.ENGLISH)
        format.calendar = calendar

        dataMap["gregorian"] = format.format(calendar.time)
    }

    /**
     * Convert prayer times into the user's preferred time format.
     *
     * Prayer times are always in 24-hour format, not because the calculator doesn't allow it, but
     * because the nafl/voluntary prayer times are calculated using the fard/necessary prayer times
     * in 24-hour formats.
     */
    fun formatTimesWithPreferences() {
        // TODO("Apply time format conversion.")
    }
}