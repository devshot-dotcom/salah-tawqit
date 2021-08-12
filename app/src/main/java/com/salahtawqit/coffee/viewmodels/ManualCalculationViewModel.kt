package com.salahtawqit.coffee.viewmodels

import android.R
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salahtawqit.coffee.helpers.CountrySet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for the [com.salahtawqit.coffee.fragments.ManualCalculationFragment].
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ManualCalculationViewModel(application: Application): AndroidViewModel(application) {

    lateinit var enteredCountry: String
    lateinit var enteredCity: String
    lateinit var addressList: MutableList<Address>
    lateinit var countrySets: Array<CountrySet>
    val isCalculationEnabled = MutableLiveData(false)
    val isCityValid = MutableLiveData(true)
    val readyToProceed = MutableLiveData(false)

    /**
     * Return an array adapter for the countries autocomplete textView.
     * Uses a static array of country names.
     * @return [ArrayAdapter]<[String]> The adapter to return.
     */
    fun getCountriesAdapter(): ArrayAdapter<String> {
        val countries = listOf("Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine")
        return ArrayAdapter(getApplication(), R.layout.simple_dropdown_item_1line, countries)
    }

    /**
     * Check whether the entered city and country name are valid.
     */
    fun validateForm() {
        val geocoder = Geocoder(getApplication())
        addressList = geocoder.getFromLocationName("$enteredCity, $enteredCountry", 1)

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

    /**
     * Parse the raw counties.json file and store it in a variable.
     */
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

    /**
     * Find a matching timezone offset for the [enteredCountry].
     * In case of no match, inform the user with an error message.
     * @return [String]. The timezone offset as a string.
     */
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

    /**
     * Map the address values to a hashmap and return.
     * @return [HashMap]<[String],[String]>
     */
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
}