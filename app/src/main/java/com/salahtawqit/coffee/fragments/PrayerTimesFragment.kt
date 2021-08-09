package com.salahtawqit.coffee.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val prayerTimesViewModel : PrayerTimesViewModel by viewModels()

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
        prayerTimesViewModel.dataMap = calculationHelperViewModel.getDataMap()

        // Perform necessary operations on the map.
        prayerTimesViewModel.calculateHijriDate()
        prayerTimesViewModel.calculateGregorianDate()
        prayerTimesViewModel.storeData()

        // Once everything has been calculated and stored, do presentational changes.
        prayerTimesViewModel.formatTimesWithPreferences()

        // Finally, bind the map to the binding class.
        binding.dataMap = prayerTimesViewModel.dataMap
    }
}