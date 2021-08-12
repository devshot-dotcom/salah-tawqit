package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.databinding.FragmentManualCalculationBinding
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import com.salahtawqit.coffee.viewmodels.ManualCalculationViewModel

/**
 * The fragment that displays manual calculation form.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ManualCalculationFragment : Fragment() {

    private lateinit var citiesEditText: EditText
    private var dataMap = hashMapOf<String, String>()
    private lateinit var countriesTextView: AutoCompleteTextView
    private lateinit var binding: FragmentManualCalculationBinding
    private val viewModel: ManualCalculationViewModel by viewModels()
    private val calculationHelperViewModel: CalculationHelperViewModel by activityViewModels()

    /**
     * Start manual calculation from the form data.
     * @param view [View]?. The form submit button, though it can be null in case of a manual trigger.
     */
    fun calculate(view: View? = null) {
        viewModel.validateForm()
    }

    /**
     * Set array adapter on the counties autocomplete textView
     */
    private fun setCountriesAdapter() {
        countriesTextView.setAdapter(viewModel.getCountriesAdapter())
    }

    /**
     * Set the editor onChange listener for the cities editText
     */
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

    /**
     * Check whether both the fields [countriesTextView] & [citiesEditText] have some text or not.
     *
     * If they both have some text, enable calculation button, disable otherwise.
     */
    private fun shouldEnableCalculation() {
        viewModel.isCalculationEnabled.value =
            countriesTextView.text.isNotEmpty() && citiesEditText.text.isNotEmpty()
    }

    /**
     * Set all sorts of listeners on the cached views.
     */
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

    /**
     * All observers for the viewModel's liveData instances at once place.
     */
    private fun setObservers() {
        // Observe city validation liveData.
        viewModel.isCityValid.observe(viewLifecycleOwner) {
            if(!it) {
                Toast.makeText(context, R.string.invalid_city, Toast.LENGTH_LONG).show()
            }
        }

        // Observe validation results.
        viewModel.readyToProceed.observe(viewLifecycleOwner) {
            if(it) {
                // Get the data map.
                dataMap = viewModel.getDataMap(dataMap)

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

        // Cache the views.
        countriesTextView = view.findViewById(R.id.countries_text_view)
        citiesEditText = view.findViewById(R.id.city_edit_text)

        setCountriesAdapter()
        setListeners()
        setObservers()

        // Read the external JSON file in a background coroutine.
        viewModel.readCountriesJson()

        // ViewModel for the binding.
        binding.viewModel = viewModel

        // Fragment for the binding.
        binding.fragment = this
    }
}