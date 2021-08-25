package com.salahtawqit.coffee.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.browse
import com.salahtawqit.coffee.databinding.FragmentLandingPageBinding
import com.salahtawqit.coffee.helpers.AutomaticCalculationHelper
import com.salahtawqit.coffee.helpers.RoomDatabaseHelper
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import com.salahtawqit.coffee.viewmodels.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The landing page that opens up right after the [SplashPageFragment].
 * Returns the landing page's layout XML file.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class LandingPageFragment : Fragment() {
    private lateinit var fragmentContext: Context
    private lateinit var binding: FragmentLandingPageBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
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

                /**
                 * In case of denial, navigate to [LocationRationaleFragment].
                 */
                if (!isGranted) {
                    findNavController().navigate(LandingPageFragmentDirections
                        .actionLandingPageFragmentToLocationRationaleFragment("automatic"))

                    return@run
                }

                /**
                 * Else navigate to [LoadingPageFragment] to start automatic calculation
                 */
                findNavController().navigate(LandingPageFragmentDirections
                    .actionLandingPageFragmentToLoadingPageFragment("automatic"))
            }
    }

    /**
     * Select the database row of calculation results and navigate to the [PrayerTimesFragment]
     * if they exist.
     */
    private fun checkExistingCalculationResults(context: Context) {
        lifecycleScope.launch(Dispatchers.IO) {
            val calculationResultsDao = RoomDatabaseHelper.getRoom(context).getCalculationResultsDao()
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

    fun navigateToPrayerTimes(v: View) {
        findNavController().navigate(LandingPageFragmentDirections
            .actionLandingPageFragmentToPrayerTimesFragment())
    }

    fun navigateToPrivacyPolicy(v: View) {
        browse(activity?.baseContext, getString(R.string.link_privacy_policy))
    }

    /**
     * Start automatic calculation.
     */
    fun initAutomaticCalc(v: View) {
        // The class handles everything.
        AutomaticCalculationHelper(fragmentContext,
            findNavController(), requestPermissionsLauncher).onButtonClick()
    }

    /**
     * Show automatic calculation dialog.
     */
    fun initAutomaticCalcHelp(v: View) {
        var descriptionDialog: DescriptionDialogFragment? = null

        // Dialog throws IllegalStateException if activity is null.
        try {
            descriptionDialog = DescriptionDialogFragment(
                title = getString(R.string.automatic_calculation),
                description = getString(R.string.automatic_calc_description)
            )
        } catch (e: IllegalStateException) {
            Toast.makeText(fragmentContext, R.string.automatic_calc_description, Toast.LENGTH_LONG).show()
        }

        descriptionDialog?.show(parentFragmentManager, "automatic-calculation-dialog")
    }

    /**
     * Start manual calculation.
     */
    fun initManualCalc(v: View) {
        findNavController().navigate(LandingPageFragmentDirections
            .actionLandingPageFragmentToManualCalculationFragment())
    }

    /**
     * Show manual calculation dialog.
     */
    fun initManualCalcHelp(v: View) {
        var descriptionDialog: DescriptionDialogFragment? = null

        // Dialog throws IllegalStateException if activity is null.
        try {
            descriptionDialog = DescriptionDialogFragment(
                title = getString(R.string.manual_calculation),
                description = getString(R.string.manual_calc_description)
            )
        } catch (e: IllegalStateException) {
            Toast.makeText(fragmentContext, R.string.manual_calc_description, Toast.LENGTH_LONG).show()
        }

        descriptionDialog?.show(parentFragmentManager, "automatic-calculation-dialog")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLandingPageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentContext = view.context

        // Check whether we're past initial fragment displacement or not.
        if(!sharedViewModel.isLandingPageDisplaced) {

            // Check if calculation results exist, navigate to the results page if so.
            checkExistingCalculationResults(view.context)
        }

        // Set binding variables.
        binding.fragment = this
        binding.areTimingsCalculated = calculationHelperViewModel.isCalculated.value

        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            // Set title every time the destination is changed.
            sharedViewModel.appBarTitle.value = getString(R.string.app_name)

            // Set label of the current destination fragment.
            sharedViewModel.currentFragmentLabel.value = destination.label?.toString()

            // Since the fragment is getting displaced.
            sharedViewModel.isLandingPageDisplaced = true
        }
    }
}