package com.salahtawqit.coffee.viewmodels

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.salahtawqit.coffee.Utilities
import com.salahtawqit.coffee.calculators.PrayerTimesCalculator
import com.salahtawqit.coffee.calculators.VoluntaryPrayersCalculator
import com.salahtawqit.coffee.helpers.PreferencesHelper
import com.salahtawqit.coffee.helpers.RoomDatabaseHelper
import java.util.*

/**
 * ViewModel that calculates prayer times and provides a hashmap to be observed.
 *
 * Perform all necessary calculations and provide a mutableMap containing all the data required by
 * the [com.salahtawqit.coffee.fragments.HomePageFragment].
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class CalculationHelperViewModel(application: Application) : AndroidViewModel(application) {
    private val utilities = Utilities()
    private lateinit var location: Location
    private val prefHelper = PreferencesHelper()
    private val geocoder = Geocoder(getApplication())
    private var dataMap: HashMap<String, String> = hashMapOf()
    val isGeocodeErred = MutableLiveData(false)
    val isCalculated = MutableLiveData(false)
    var isJustLaunched = true

    // Calculate prayer timings.
    private fun calculatePrayerTimes(
        timezone: Long?, dataMap: HashMap<String, String>,
        latitude: Double?, longitude: Double?
    ): HashMap<String, String> {
        val calculator = PrayerTimesCalculator()
        prefHelper.setCalculator(calculator)

        // Get values based on user preferences.
        calculator.timeFormat = calculator.Time24
        calculator.calcMethod = prefHelper.getCalculationMethod()
        calculator.asrJuristic = prefHelper.getJurisdiction()
        calculator.adjustHighLats = prefHelper.getLatAdjustment()

        // Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha
        val offsets = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        calculator.tune(offsets)

        // Instance of current time.
        val cal = Calendar.getInstance()
        cal.time = Date()

        // Map all the data to be presented to the user.
        val prayerNames = calculator.timeNames
        val prayerTimes = calculator.getPrayerTimes(
            cal, latitude, longitude, timezone?.toDouble())

        // Enter prayer times into the dataMap.
        for (i in prayerTimes.indices) {
            dataMap[prayerNames[i]] = prayerTimes[i]
        }

        return dataMap
    }

    /**
     * Use [VoluntaryPrayersCalculator] to calculate voluntary prayer times.
     */
    private fun calculateAdditionalTimes(dataMap: HashMap<String, String>): HashMap<String, String> {
        val voluntaryPrayersCalculator = VoluntaryPrayersCalculator()
        voluntaryPrayersCalculator.setDataMap(dataMap)

        dataMap["Tahajjud"] = voluntaryPrayersCalculator.getTahajjudTime()
        dataMap["Ishraq"] = voluntaryPrayersCalculator.getIshraqTime()
        dataMap["Duha"] = voluntaryPrayersCalculator.getDuhaTime()

        return dataMap
    }

    /**
     * Geocode location co-ordinates into an address.
     *
     * Checks whether the geocoded location is null or not. If it is null, the LiveData instance
     * [isGeocodeErred] is mutated to inform the observer in LoadingPageFragment.
     *
     * @return [Address]. The geocoded location address.
     */
    private fun reverseGeocode(): Address {
        val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        if(addressList == null) {
            isGeocodeErred.value = true
        }

        if(addressList[0] == null) {
            isGeocodeErred.value = true
        }

        return addressList[0]
    }

    /**
     * Make preparations for automatic calculation and then call [calculatePrayerTimes].
     */
    fun automaticallyCalculate() {
        val address = reverseGeocode()

        // Map address to dataMap.
        dataMap["city"] = address.locality
        dataMap["country"] = address.countryName
        dataMap["lat"] = address.latitude.toString()
        dataMap["lon"] = address.longitude.toString()

        // Map prayer times to dataMap.
        dataMap = calculatePrayerTimes(
            utilities.getTimezoneOffset(), dataMap, location.latitude, location.longitude)

        // Map additional times to dataMap.
        dataMap = calculateAdditionalTimes(dataMap)

        // LiveData instance that's being observed by the observer.
        isCalculated.value = true
    }

    /**
     * Make preparations for manual calculation and then call [calculatePrayerTimes].
     */
    fun manuallyCalculate() {
        // Map prayer times to dataMap.
        dataMap = calculatePrayerTimes(
            dataMap["timezone"]?.toDouble()?.toLong(),
            dataMap, dataMap["lat"]?.toDouble(), dataMap["lon"]?.toDouble())

        // Map additional times to dataMap.
        dataMap = calculateAdditionalTimes(dataMap)

        // LiveData instance that's being observed by the observer.
        isCalculated.value = true
    }

    // Getters.
    fun getDataMap(): HashMap<String, String> {
        return this.dataMap
    }

    // Setters.
    fun setLocation(location: Location) {
        this.location = location
    }

    fun setDataMap(dataMap: HashMap<String, String>) {
        this.dataMap = dataMap
    }

    /**
     * Set the dataMap entries using the list provided by the database.
     * @param list [RoomDatabaseHelper.CalculationResults]. The list of database entities to map.
     */
    fun setDataMapFromEntities(list: List<RoomDatabaseHelper.CalculationResults>) {
        val calculationResults = list[0]

        dataMap["city"] = calculationResults.city.toString()
        dataMap["country"] = calculationResults.country.toString()
        dataMap["lat"] = calculationResults.latitude.toString()
        dataMap["lon"] = calculationResults.longitude.toString()
        dataMap["Tahajjud"] = calculationResults.tahajjud.toString()
        dataMap["Fajr"] = calculationResults.fajr.toString()
        dataMap["Sunrise"] = calculationResults.sunrise.toString()
        dataMap["Ishraq"] = calculationResults.ishraq.toString()
        dataMap["Duha"] = calculationResults.duha.toString()
        dataMap["Dhuhr"] = calculationResults.dhuhr.toString()
        dataMap["Asr"] = calculationResults.asr.toString()
        dataMap["Sunset"] = calculationResults.sunset.toString()
        dataMap["Maghrib"] = calculationResults.maghrib.toString()
        dataMap["Isha"] = calculationResults.isha.toString()

        /**
         * Finally, set the [isCalculated] to true to inform the observer that results have been
         * calculated.
         */
        isCalculated.postValue(true)
    }
}