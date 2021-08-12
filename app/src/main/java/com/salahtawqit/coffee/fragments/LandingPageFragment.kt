package com.salahtawqit.coffee.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.helpers.AutomaticCalculationHelper
import com.salahtawqit.coffee.helpers.RoomDatabaseHelper
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The landing page that opens up right after the [SplashPageFragment].
 * Returns the landing page's layout XML file.
 * Is a part of a ViewPager2 in the [HomePageFragment].
 -* @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class LandingPageFragment : Fragment() {
    private lateinit var autoCalcButton: TextView
    private lateinit var manualCalcButton: TextView
    private lateinit var autoCalcHelpButton: ImageButton
    private lateinit var manualCalcHelpButton: ImageButton
    private val calculationHelperViewModel: CalculationHelperViewModel by activityViewModels()

    /**
     * Register for the permission result.
     *
     * If you register after fragment creation, it'll throw an error.
     *
     * Error Statement: Fragment is attempting to registerForActivityResult after being created.
     * Fragments must call registerForActivityResult() before they are created.
     * */
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
            isGranted -> run {
                /** In case of denial, navigate to [LocationRationaleFragment]. */
                if (!isGranted) findNavController().navigate(LandingPageFragmentDirections
                    .actionLandingPageFragmentToLocationRationaleFragment("automatic"))
            }
    }

    /**
     * Select the database row of calculation results and navigate to the [PrayerTimesFragment] if
     * they exist.
     */
    private fun checkExistingCalculationResults(context: Context) {
        lifecycleScope.launch(Dispatchers.IO) {
            val calculationResultsDao = RoomDatabaseHelper.getRoom(context).calculationResultsDao()
            val selectionResults = calculationResultsDao.selectAll()

            if(selectionResults.isNotEmpty()) {
                // Set the dataMap from the selected list of calculation results.
                calculationHelperViewModel.setDataMapFromEntities(selectionResults)

                activity?.runOnUiThread {
                    // Navigate to the prayer times fragment.
                    findNavController().navigate(LandingPageFragmentDirections
                        .actionLandingPageFragmentToPrayerTimesFragment())
                }
            }
        }
    }

    /**
     * Initialize all lateinit variables.
     */
    private fun initLateInit(view: View) {
        // Cache the views.
        autoCalcButton = view.findViewById(R.id.home_auto_button)
        manualCalcButton = view.findViewById(R.id.home_manual_button)
        autoCalcHelpButton = view.findViewById(R.id.home_auto_button_help)
        manualCalcHelpButton = view.findViewById(R.id.home_manual_button_help)
    }

    /**
     * Set all sorts of listeners.
     */
    private fun setListeners(view: View) {
        autoCalcButton.setOnClickListener {
            // The class handles everything.
            AutomaticCalculationHelper(view.context,
                findNavController(), requestPermissionsLauncher).onButtonClick()
        }
        manualCalcButton.setOnClickListener {
            findNavController().navigate(LandingPageFragmentDirections
                .actionLandingPageFragmentToManualCalculationFragment())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If the fragment is called on app launched, check database.
        if(calculationHelperViewModel.isJustLaunched) {
            // Check if calculation results exist, navigate to the results page if so.
            checkExistingCalculationResults(view.context)
        }

        initLateInit(view)
        setListeners(view)
    }
}