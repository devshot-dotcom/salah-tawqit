package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.salahtawqit.coffee.R

/**
 * The fragment that displays the form for manual prayer times calculation.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ManualCalculationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_calculation, container, false)
    }
}