package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import com.salahtawqit.coffee.databinding.FragmentPrayerTimesBinding
import com.salahtawqit.coffee.hideParent
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import com.salahtawqit.coffee.viewmodels.PrayerTimesViewModel
import com.salahtawqit.coffee.viewmodels.SharedViewModel

/**
 * The fragment that displays the prayer times.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class PrayerTimesFragment : Fragment() {
    private lateinit var binding: FragmentPrayerTimesBinding
    private val calculationHelperViewModel: CalculationHelperViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: PrayerTimesViewModel by viewModels()

    fun navigateBackwards(v: View) {
        findNavController().popBackStack()
    }

    fun hideSavedTimesNotification(v: View) {
        v.hideParent()

        // Set the identifier that tells whether database is used or not.
        calculationHelperViewModel.areTimingsSelectedFromDatabase = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPrayerTimesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If settings are updated, re-calculate prayer times and inform in case of failure.
        if(sharedViewModel.areSettingsUpdated) {
            if(!calculationHelperViewModel.reCalculate()) {
                Toast.makeText(this.context, "Error updating settings, please contact us to report the issue."
                    , Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.set(map = calculationHelperViewModel.getDataMap())
        viewModel.startWorking()

        viewModel.viewData.observe(viewLifecycleOwner) {
            if(it != null) {
                // Set binding data.
                binding.fragment = this
                binding.viewData = it
                binding.areTimingsSelectedFromDatabase =
                    calculationHelperViewModel.areTimingsSelectedFromDatabase
            }
        }
    }
}
