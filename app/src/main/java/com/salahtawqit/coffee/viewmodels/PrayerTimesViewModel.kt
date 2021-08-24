package com.salahtawqit.coffee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.helpers.PreferencesHelper
import com.salahtawqit.coffee.helpers.RoomDatabaseHelper
import com.salahtawqit.coffee.helpers.TimeFormatHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel for the corresponding fragment. generate a [map] and store it's data in the database.
 * Calculate hijri and gregorian date based on the [map]. Format prayer times into the user's
 * preferred time format.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class PrayerTimesViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Data to be displayed by the DataBinding of the corresponding fragment.
     * Observe it to gain access to the [ViewData] attributes.
     */
    val viewData = MutableLiveData<ViewData>()

    /**
     * A map that contains prayer times, location details, and date/time information for the user.
     */
    private var map = hashMapOf<String, String>()

    /**
     * A collection of all the prayer times needed by the [ViewData] for the DataBinding of the
     * corresponding fragment.
     */
    data class PrayerTimes(
        var tahajjud: String?,
        var fajr: String?,
        var sunrise: String?,
        var ishraq: String?,
        var duha: String?,
        var dhuhr: String?,
        var asr: String?,
        var sunset: String?,
        var maghrib: String?,
        var isha: String?
    )

    /**
     * Collection of data to be displayed by the DataBinding of the corresponding fragment.
     * @param hijriDate [String], the current date in the Islamic Calendar.
     * @param gregorianDate [String], the current date in the Gregorian Calendar.
     * @param cityName [String], the city for which the prayer times belong to.
     * @param countryName [String], the country for which the city belongs to.
     * @param prayerTimes [PrayerTimes], the collection of formatted prayer times.
     */
    data class ViewData(
        val hijriDate: String,
        val gregorianDate: String,
        val cityName: String?,
        val countryName: String?,
        val prayerTimes: PrayerTimes
    )

    /**
     * Use Room to store data in the database.
     * @param map [HashMap]<[String], [String]>. HashMap to store in the database.
     */
    private fun store(map: HashMap<String, String>) {

        // Database access must be limited to a background thread hence we use coroutines.
        viewModelScope.launch(Dispatchers.IO) {
            val dao = RoomDatabaseHelper.getRoom(getApplication()).getCalculationResultsDao()
            val calculationResults = RoomDatabaseHelper.CalculationResults(
                id = 1,
                latitude = map["lat"],
                longitude = map["lon"],
                city = map["city"],
                country = map["country"],
                timezone = map["timezone"]?.toDouble(),
                tahajjud = map["Tahajjud"],
                fajr = map["Fajr"],
                sunrise = map["Sunrise"],
                ishraq = map["Ishraq"],
                duha = map["Duha"],
                dhuhr = map["Dhuhr"],
                asr = map["Asr"],
                sunset = map["Sunset"],
                maghrib = map["Maghrib"],
                isha = map["Isha"],
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
     * Calculate the Hijri date and add it to the map.
     * @return [String]. The calculated hijri date.
     */
    private fun getHijriDate(): String {
        val calendar = UmmalquraCalendar()
        val sdf = SimpleDateFormat("d MMMM, y", Locale.ENGLISH)

        // Calendar somehow shows a day less and parses a day more, adding 0 fixes this.
        calendar.add(UmmalquraCalendar.DATE, 0)

        // Adjust date using preferred adjustment.
        when(PreferencesHelper(getApplication()).getHijriDate()) {
            "1" -> calendar.add(UmmalquraCalendar.DATE, 1)
            "2" -> calendar.add(UmmalquraCalendar.DATE, 2)
            "3" -> calendar.add(UmmalquraCalendar.DATE, -1)
            "4" -> calendar.add(UmmalquraCalendar.DATE, -2)
        }

        sdf.calendar = calendar

        return sdf.format(calendar.time)
    }

    /**
     * Calculate the Gregorian date and add it to the map.
     * @return [String]. The calculated gregorian date.
     */
    private fun getGregorianDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("d MMMM, y", Locale.ENGLISH)
        format.calendar = calendar

        return format.format(calendar.time)
    }

    /**
     * Convert prayer times into the user's preferred time format.
     *
     * Prayer times are always in 24-hour format, not because the calculator doesn't allow it, but
     * because the nafl/voluntary prayer times are calculated using the fard/necessary prayer times
     * in 24-hour formats.
     *
     * Generates an instance of [PrayerTimes] and applies time format conversion on each time string,
     * retrieved from the [map]
     *
     * @return [PrayerTimes]. Formatted prayer times as per user preference.
     */
    private fun getFormattedPrayerTimes(): PrayerTimes {
        val prayerTimes = PrayerTimes(
            tahajjud = map["Tahajjud"],
            fajr = map["Fajr"],
            sunrise = map["Sunrise"],
            ishraq = map["Ishraq"],
            duha = map["Duha"],
            dhuhr = map["Dhuhr"],
            asr = map["Asr"],
            sunset = map["Sunset"],
            maghrib = map["Maghrib"],
            isha = map["Isha"],
        )

        // Apply time format conversion.
        PreferencesHelper(getApplication()).getTimeFormat()?.let {
            val timeFormatHelper = TimeFormatHelper(
                preferredFormat = it,
                timeFormatKeys = getApplication<Application>().resources.getStringArray(R.array.time_format_values)
            )

            prayerTimes.tahajjud = timeFormatHelper.getFormattedTime(prayerTimes.tahajjud)
            prayerTimes.fajr = timeFormatHelper.getFormattedTime(prayerTimes.fajr)
            prayerTimes.sunrise = timeFormatHelper.getFormattedTime(prayerTimes.sunrise)
            prayerTimes.ishraq = timeFormatHelper.getFormattedTime(prayerTimes.ishraq)
            prayerTimes.duha = timeFormatHelper.getFormattedTime(prayerTimes.duha)
            prayerTimes.dhuhr = timeFormatHelper.getFormattedTime(prayerTimes.dhuhr)
            prayerTimes.asr = timeFormatHelper.getFormattedTime(prayerTimes.asr)
            prayerTimes.sunset = timeFormatHelper.getFormattedTime(prayerTimes.sunset)
            prayerTimes.maghrib = timeFormatHelper.getFormattedTime(prayerTimes.maghrib)
            prayerTimes.isha = timeFormatHelper.getFormattedTime(prayerTimes.isha)
        }

        return prayerTimes
    }

    fun startWorking() {
        store(map)

        viewData.value = ViewData(
            hijriDate = getHijriDate(),
            gregorianDate = getGregorianDate(),
            cityName = map["city"],
            countryName = map["country"],
            prayerTimes = getFormattedPrayerTimes()
        )
    }

    fun set(map: HashMap<String, String>) {
        this.map = map
    }
}