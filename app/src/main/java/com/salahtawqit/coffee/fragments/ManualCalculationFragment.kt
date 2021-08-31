package com.salahtawqit.coffee.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.databinding.FragmentManualCalculationBinding
import com.salahtawqit.coffee.helpers.GeocoderException
import com.salahtawqit.coffee.helpers.InvalidCityException
import com.salahtawqit.coffee.helpers.ListAdapter
import com.salahtawqit.coffee.helpers.NetworkException
import com.salahtawqit.coffee.hideKeyboard
import com.salahtawqit.coffee.isAnIndexEmpty
import com.salahtawqit.coffee.toEditable
import com.salahtawqit.coffee.viewmodels.ManualCalculationViewModel
import com.salahtawqit.coffee.viewmodels.SharedViewModel

/**
 * The fragment that displays manual calculation form.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ManualCalculationFragment : Fragment() {

    /*private lateinit var citiesEditText: EditText
    private lateinit var fragmentContext: Context
    private var dataMap = hashMapOf<String, String>()
    private lateinit var historyItems: RecyclerView
    private lateinit var countriesTextView: AutoCompleteTextView
    private lateinit var binding: FragmentManualCalculationBinding
    private val viewModel: ManualCalculationViewModel by viewModels()
    private val calculationHelperViewModel: CalculationHelperViewModel by activityViewModels()*/

    /**
     * Start manual calculation from the form data.
     * @param view [View]?. The form submit button, though it can be null in case of a manual trigger.
     */
    /*fun calculate() {
        this.hideKeyboard()
        viewModel.validateForm()
    }

    private fun setCountriesAdapter() {
        val countries = listOf("Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine")
        countriesTextView.setAdapter(ArrayAdapter
            (fragmentContext, android.R.layout.simple_dropdown_item_1line, countries))
    }

    private fun setRecentSearchesAdapter() {
        val listAdapter = ListAdapter(
            inflater = layoutInflater,
            textViewResId = R.id.list_title,
            dataList = viewModel.historyItems,
            layoutResId = R.layout.template_list_item_history,
            listener = object: ListAdapter.ItemClickListener {
                override fun onItemClick(view: View, position: Int) {

                    // Split the string from `city, country` format.
                    val locationList = viewModel.historyItems[position].split(",")

                    // Assign the parts to views.
                    citiesEditText.text = locationList[0].toEditable()
                    countriesTextView.text = locationList[1].toEditable()
                }
            }
        )

        // Necessary step.
        historyItems.layoutManager = LinearLayoutManager(this.context)

        // Set the adapter.
        historyItems.adapter = listAdapter
    }

    *//**
     * Set the editor onChange listener for the cities editText
     *//*
    private fun setCitiesActionListener() {
        citiesEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    calculate()
                    false
                }
                else -> false
            }
        }
    }

    *//**
     * Check whether both the fields [countriesTextView] & [citiesEditText] have some text or not.
     *
     * If they both have some text, enable calculation button, disable otherwise.
     *//*
    private fun shouldEnableCalculation() {
        viewModel.isCalculationEnabled.value =
            countriesTextView.text.isNotEmpty() && citiesEditText.text.isNotEmpty()
    }

    *//**
     * Set all sorts of listeners on the cached views.
     *//*
    private fun setListeners() {
        setCitiesActionListener()

        // Set viewModel country name on text changed.
        countriesTextView.doOnTextChanged { text, _, _, _ ->
            viewModel.enteredCountry = text.toString()
            shouldEnableCalculation()
        }

        // Set viewModel city name on text changed.
        citiesEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.enteredCity = text.toString()

            // Reset field errors (if any).
            viewModel.isCityValid.value = true
            shouldEnableCalculation()
        }
    }

    *//**
     * All observers for the viewModel's liveData instances at once place.
     *//*
    private fun setObservers() {
        // Observe city validation.
        viewModel.isCityValid.observe(viewLifecycleOwner) {
            if(!it) {
                Toast.makeText(
                    context,
                    R.string.invalid_city,
                    Toast.LENGTH_LONG)
                    .show()
            }
        }

        // Observe the geocoder.
        viewModel.isGeocodeErred.observe(viewLifecycleOwner) {
            if(it) {
                Toast.makeText(context,
                    "The geocoder faced an internal problem, please try again.",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }

        // Observe recent searches' existence.
        viewModel.doRecentSearchesExist.observe(viewLifecycleOwner) {
            if(it) setRecentSearchesAdapter()
        }

        // Observe validation results.
        viewModel.readyToProceed.observe(viewLifecycleOwner) {
            if(it) {
                dataMap = viewModel.getDataMap(dataMap)
                viewModel.storeLocation()

                // In case the timezone is found.
                if(dataMap["timezone"]?.isNotEmpty() == true) {

                    // Pass the data map to the other view model.
                    calculationHelperViewModel.setDataMap(dataMap)

                    // Navigate to the loading page fragment.
                    findNavController().navigate(ManualCalculationFragmentDirections
                        .actionManualCalculationFragmentToLoadingPageFragment("manual"))

                    return@observe
                }

                // Otherwise, ask the user to use automatic calculation mode.
                Toast.makeText(context, R.string.error_timezone_not_found, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentContext = view.context

        // Cache the views.
        citiesEditText = view.findViewById(R.id.city_edit_text)
        countriesTextView = view.findViewById(R.id.countries_text_view)
        historyItems = view.findViewById(R.id.recent_searches_list)

        setListeners()
        setObservers()
        setCountriesAdapter()
        viewModel.checkRecentSearchesExistence()

        // Read the external JSON file in a background coroutine.
        viewModel.readCountriesJson()

        // ViewModel for the binding.
        binding.viewModel = viewModel

        // Fragment for the binding.
        binding.fragment = this
    }*/

    private lateinit var binding: FragmentManualCalculationBinding
    private var countriesView: AutoCompleteTextView? = null
    private var citiesView: EditText? = null
    private var historyRecyclerView: RecyclerView? = null
    private val viewModel: ManualCalculationViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    /**
     * The parent activity's context, initialized in onViewCreated
     * using the parametric View instance.
     */
    private lateinit var activityContext: Context

    /**
     * Change viewModel's isCalculationEnabled value
     * if city and country name are both not empty.
     *
     * Also, make the isCityValid true so that previously
     * shown errors can be hidden.
     */
    private val textWatcher = object: TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        /**
         * This method is called to notify you that, within `s`,
         * the `count` characters beginning at `start`
         * have just replaced old text that had length `before`.
         * It is an error to attempt to make changes to `s` from
         * this callback.
         */
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.isCalculationEnabled.value =
                !isAnIndexEmpty(listOf(countriesView?.text.toString(), citiesView?.text.toString()))

            viewModel.isCityValid.value = true
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    fun startGeocoding() {
        this.hideKeyboard()

        try {
            val address = viewModel.getGeocodedAddress(
                country = countriesView?.text.toString(),
                city = citiesView?.text.toString())

            viewModel.store(address)
            sharedViewModel.manualAddress = address

            findNavController().navigate(ManualCalculationFragmentDirections
                .actionManualCalculationFragmentToLoadingPageFragment("manual"))
        } catch (e: Exception) {
            when(e) {
                is NetworkException -> {
                    Toast.makeText(activityContext, getString(
                        R.string.network_connection_too_slow), Toast.LENGTH_LONG).show()
                }
                is GeocoderException -> {
                    Toast.makeText(activityContext, getString(
                        R.string.geocoder_failure_message), Toast.LENGTH_LONG).show()
                }
                is InvalidCityException -> {
                    Toast.makeText(activityContext, getString(
                        R.string.invalid_city), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Initialize the views and attach required listeners to them.
     * @param parent [View], the parent activity's layout.
     */
    private fun initViews(parent: View) {
        countriesView = parent.findViewById(R.id.countries_text_view)
        citiesView = parent.findViewById(R.id.city_edit_text)
        historyRecyclerView = parent.findViewById(R.id.recent_searches_list)

        countriesView?.addTextChangedListener(textWatcher)
        citiesView?.addTextChangedListener(textWatcher)

        // Do what the submit button does on keyboard `done` button.
        citiesView?.setOnEditorActionListener { _, actionId, _ ->

            /*
            * Returning false means that we are not handling the callback,
            * and the activity should manage it itself.
            *
            * Then why do we return false while handling the callback?
            * So the activity can hide the keyboard itself.
            */
            return@setOnEditorActionListener when(actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    startGeocoding()
                    false
                }
                else -> false
            }
        }
    }

    /**
     * Initialize the adapters for [countriesView] and [historyRecyclerView]
     * and set them on the views.
     */
    private fun initViewAdapters() {
        val countries = listOf("Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine")
        countriesView?.setAdapter(ArrayAdapter
            (activityContext, android.R.layout.simple_dropdown_item_1line, countries))

        /* History Items/Recent Searches adapter. */
        viewModel.historyItems.observe(viewLifecycleOwner) {
            it?.let {
                val listAdapter = ListAdapter(
                    inflater = layoutInflater,
                    textViewResId = R.id.list_title,
                    dataList = it,
                    layoutResId = R.layout.template_list_item_history,
                    listener = object: ListAdapter.ItemClickListener {
                        override fun onItemClick(view: View, position: Int) {

                            // Split the string from `city, country` format.
                            val strParts = it[position].split(",")

                            // Assign the parts to views.
                            citiesView?.text = strParts[0].toEditable()
                            countriesView?.text = strParts[1].toEditable()
                        }
                    }
                )

                // Necessary step.
                historyRecyclerView?.layoutManager = LinearLayoutManager(activityContext)

                // Set the adapter.
                historyRecyclerView?.adapter = listAdapter
            }
        }
    }

    private fun setBindingVars() {
        binding.fragment = this
        binding.viewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManualCalculationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityContext = view.context
        initViews(parent = view)
        initViewAdapters()
        setBindingVars()
    }
}
