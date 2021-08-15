package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    /**
     * Pop back stack.
     */
    fun navigateBackwards(v: View) {
        findNavController().popBackStack()
    }

    fun hideNotification(v: View) {
        val notification = v.parent as LinearLayout
        notification.visibility = View.GONE

        // Set the identifier that tells whether database is used or not.
        calculationHelperViewModel.isSelectedFromDb = false
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
        viewModel.dataMap = calculationHelperViewModel.getDataMap()

        // Store the data in the database.
        viewModel.storeData()

        // Calculate the dates.
        viewModel.calculateHijriDate()
        viewModel.calculateGregorianDate()

        // Once everything has been calculated and stored, do presentational changes.
        viewModel.formatTimesWithPreferences()

        // Set binding variables.
        binding.fragment = this
        binding.dataMap = viewModel.dataMap
        binding.isSelectedFromDb = calculationHelperViewModel.isSelectedFromDb
    }
}