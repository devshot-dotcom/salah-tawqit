package com.salahtawqit.coffee.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.salahtawqit.coffee.R

/**
 * The rationale to show when access to location is denied.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class LocationRationaleFragment : Fragment() {
    private val args : LocationRationaleFragmentArgs by navArgs()

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
                /** In case it's granted, navigate to [LoadingPageFragment]. */
                if (isGranted) findNavController().navigate(LocationRationaleFragmentDirections
                    .actionLocationRationaleFragmentToLoadingPageFragment(args.calculationMode))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_rationale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val grantLocationButton = activity?.findViewById<TextView>(R.id.rationale_grant_button)
        val manualSelectButton = activity?.findViewById<TextView>(R.id.rationale_manual_button)

        grantLocationButton?.setOnClickListener{
            // Show request permission dialog.
            requestPermissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        manualSelectButton?.setOnClickListener{
            // TODO: Navigate to [ManualSearchFragment]
        }
    }
}