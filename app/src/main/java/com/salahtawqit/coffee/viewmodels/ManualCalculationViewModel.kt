package com.salahtawqit.coffee.viewmodels

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

/**
 * ViewModel for the [com.salahtawqit.coffee.fragments.ManualCalculationFragment].
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ManualCalculationViewModel(application: Application): AndroidViewModel(application) {

    /*var enteredCity: String = ""
    var enteredCountry: String = ""
    lateinit var countrySets: Array<CountrySet>
    lateinit var recentSearchesList: List<String>
    val isCityValid = MutableLiveData(true)
    private var addressList = mutableListOf(Address(Locale.getDefault()))
    val readyToProceed = MutableLiveData(false)
    val isGeocodeErred = MutableLiveData(false)
    val isCalculationEnabled = MutableLiveData(false)
    val doRecentSearchesExist = MutableLiveData(false)*/

    /**
     * Check whether the entered city and country name are valid.
     */
    /*fun validateForm() {
        val geocoder = Geocoder(getApplication())

        try {
            // Geocoder can fail and throw IOException for some internal unidentified reason.
            addressList = geocoder.getFromLocationName("$enteredCity, $enteredCountry", 1)
        } catch (e: IOException) {

            // Inform the observers.
            isGeocodeErred.value = true
            return
        }

        // In case of a bad location.
        if(addressList.isEmpty()) {

            // Geocode using the city name alone.
            addressList = geocoder.getFromLocationName(enteredCity, 1)

            // The worst case.
            if(addressList.isEmpty()) {
                isCityValid.value = false
                return
            }
        }

        // Second check iteration.
        if(addressList.first().locality == null) {
            isCityValid.value = false
            return
        }

        // Notify the observers that we're ready to proceed.
        readyToProceed.value = true
    }

    *//**
     * Parse the raw counties.json file and store it in a variable.
     *//*
    fun readCountriesJson() {
        // Launch a coroutine.
        viewModelScope.launch(Dispatchers.IO) {

            // Read external raw JSON file.
            getApplication<Application>().resources
                .openRawResource(com.salahtawqit.coffee.R.raw.countries).bufferedReader().use {

                // Parse the JSON string using GSON.
                countrySets = Gson().fromJson(it, Array<CountrySet>::class.java)
            }
        }
    }

    *//**
     * Find a matching timezone offset for the [enteredCountry].
     * In case of no match, inform the user with an error message.
     * @return [String]. The timezone offset as a string.
     *//*
    private fun getTimezoneOffset(): String {

        // Iterate over the generated array of country sets.
        for(countrySet in countrySets) {

            // When countries match, return the matching timezone offset.
            if(enteredCountry == countrySet.getName()) {
                return countrySet.getTimezoneOffset()
            }
        }

        return ""
    }

    *//**
     * Store the searched location in database.
     *//*
    fun storeLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val recentSearchesDao = RoomDatabaseHelper
                .getRoom(getApplication()).getRecentSearchesDao()

            recentSearchesDao.insert(RoomDatabaseHelper
                .RecentSearch(null, city = enteredCity, country = enteredCountry))
        }
    }

    *//**
     * Map the address values to a hashmap and return.
     * @return [HashMap]<[String],[String]>
     *//*
    fun getDataMap(dataMap: HashMap<String, String>): HashMap<String, String> {
        val address = addressList.first()

        // Assign the country and city name to the relevant variables for later use.
        enteredCity = address.locality
        enteredCountry = address.countryName

        // Push the entries to the map.
        dataMap["city"] = enteredCity
        dataMap["country"] = enteredCountry
        dataMap["lat"] = address.latitude.toString()
        dataMap["lon"] = address.longitude.toString()
        dataMap["timezone"] = getTimezoneOffset()

        return dataMap
    }

    *//**
     * Do recent searches exist in the database?
     *//*
    fun checkRecentSearchesExistence() {
        viewModelScope.launch(Dispatchers.IO) {
            val recentSearchesDao = RoomDatabaseHelper
                .getRoom(getApplication()).getRecentSearchesDao()

            val selectionResults = recentSearchesDao.selectAll()

            if(selectionResults.isNotEmpty()) {

                // Make a mutable list.
                val mutableList = mutableListOf<String>()

                // Add items to that list.
                selectionResults.forEach {
                    mutableList.add("${it.city}, ${it.country}")
                }

                // Assign the list to the globally available list.
                recentSearchesList = mutableList

                // Inform the observers.
                doRecentSearchesExist.postValue(true)
            }
        }
    }*/

    val isCalculationEnabled = MutableLiveData(false)
    val isCityValid = MutableLiveData(true)
    val isHistoryEmpty = MutableLiveData(true)
    private val geocoder = Geocoder(getApplication())

    /**
     * Geocode a location and return an address.
     * @param country [String], the country name.
     * @param city [String], the city name.
     * @return [Address], the geocoded address instance.
     */
    fun getGeocodedAddress(country: String, city: String): Address {
        val addressList = geocoder.getFromLocationName("$city, $country", 1)
        throw Exception()
    }
}