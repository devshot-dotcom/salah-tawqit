package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.salahtawqit.coffee.databinding.FragmentPrayerTimesBinding
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import com.salahtawqit.coffee.viewmodels.PrayerTimesViewModel

/**
 * The fragment that displays the prayer times.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class PrayerTimesFragment : Fragment() {
    private lateinit var binding: FragmentPrayerTimesBinding
    private val calculationHelperViewModel: CalculationHelperViewModel by activityViewModels()
    private val viewModel: PrayerTimesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrayerTimesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set isJustLaunched to false as now we're not on the first screen.
        calculationHelperViewModel.isJustLaunched = false

        viewModel.dataMap = calculationHelperViewModel.getDataMap()

        // Store the data in the database.
        viewModel.storeData()

        // Calculate the dates.
        viewModel.calculateHijriDate()
        viewModel.calculateGregorianDate()

        // Once everything has been calculated and stored, do presentational changes.
        viewModel.formatTimesWithPreferences()

        // Finally, bind the map to the binding class.
        binding.dataMap = viewModel.dataMap
    }
}