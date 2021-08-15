package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.hasPermission
import com.salahtawqit.coffee.isConnectedToInternet

/**
 * Display Internet Connectivity Error.
 */
class NetworkErrorFragment : Fragment() {
    private lateinit var retryButton: TextView

    /** Initialize all lateinit variables. */
    private fun initLateInit(view: View) {
        retryButton = view.findViewById(R.id.network_retry_button)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLateInit(view)

        val args: NetworkErrorFragmentArgs by navArgs()

        retryButton.setOnClickListener {
            if(!isConnectedToInternet(view.context)) {
                retryButton.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.wobble))
                return@setOnClickListener
            }

            /** Check if calculationMode is automatic and location permission isn't granted */
            if(args.calculationMode == "automatic") {
                if(!hasPermission(view.context, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    /** Pass on the received calculationMode to [LocationRationaleFragment] */
                    findNavController().navigate(NetworkErrorFragmentDirections
                        .actionNetworkErrorFragmentToLocationRationaleFragment(args.calculationMode))
                    return@setOnClickListener
                }
            }

            /** Else pass on the received calculationMode to [LoadingPageFragment] */
            findNavController().navigate(NetworkErrorFragmentDirections
                .actionNetworkErrorFragmentToLoadingPageFragment(args.calculationMode))
        }
    }
}